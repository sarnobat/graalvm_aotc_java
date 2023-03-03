package com;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilySpouse;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualReference;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.parser.GedcomParser;

/*

gedcom2mwk.osx ~/sarnobat.git/genealogy/rohidekar.ged
gedcom2mwk.osx ~/sarnobat.git/genealogy/rohidekar.ged | pandoc --from mediawiki --to html | tee /tmp/index.html

groovy -Dged=$HOME/sarnobat.git/genealogy/rohidekar.ged -Droot=I30 -Dindent='*'  ~/bin/gedcom_list.groovy
groovy -Dged=$HOME/sarnobat.git/genealogy/sarnobat.ged  ~/bin/gedcom_list.groovy

*/
// TODO (2021-05-31): this isn't working for the youngest generation. 
// Ideally port to Golang, but elliottchance's library doesn't generate a hierarchical 
// structure so I'd have to add that myself. Would be a good experience
public class GedcomList {

    public static void main(String[] args) throws FileNotFoundException, IOException, GedcomParserException {

        String star = System.getProperty("indent", "*");

        GedcomParser gedcomParser = new GedcomParser();
        String ged;
        //I30
        String root = "I210";

        if (true) {
            if (args.length == 0) {
                ged = System.getProperty("user.home") + "/" + "sarnobat.git/genealogy/sarnobat.ged";
            } else {
                ged = args[0];
                //root = System.getProperty("root", "I210");
                root = System.getProperty("root", "I30");
            }
           
        } else { // JIT
            // With graalvm native image this gets ignored despite what the documentation
            // claims
            if (System.getProperty("ged") != null) {
                ged = System.getProperty("ged");
            } else {
                // rohidekar.get has an issue
                // https://github.com/frizbog/gedcom4j/blob/master/src/main/java/org/gedcom4j/parser/LinePieces.java
//                org.gedcom4j.exception.GedcomParserException: All GEDCOM lines are required to have a tag value, but no tag could be found on line 21
//                at org.gedcom4j.parser.LinePieces.processTag(LinePieces.java:148)
//                at org.gedcom4j.parser.LinePieces.<init>(LinePieces.java:86)
//                at org.gedcom4j.parser.StringTreeBuilder.addNewNode(StringTreeBuilder.java:172)
//                at org.gedcom4j.parser.StringTreeBuilder.appendLine(StringTreeBuilder.java:158)
//                at org.gedcom4j.parser.GedcomParser.load(GedcomParser.java:324)
//                at org.gedcom4j.parser.GedcomParser.load(GedcomParser.java:350)
//                at com.GedcomList.main(GedcomList.java:55)
                ged = System.getProperty("user.home") + "/" + "/sarnobat.git/genealogy/sarnobat.ged";
//                              "/sarnobat.git/genealogy/rohidekar.ged"));
            }
        }
        System.err.println("GedcomList.main() ged = " + ged);
        gedcomParser.load(ged);
        Gedcom g = gedcomParser.getGedcom();

        Map<String, Individual> individuals = g.getIndividuals();

        if (false) { // Move this to a separate program
            // Individual root = null;
            for (String id : individuals.keySet()) {
                Individual ind = individuals.get(id);
                System.out.println(id + "\t" + ind.getFormattedName());
                if (ind.getFamiliesWhereChild() == null
                        || ind.getFamiliesWhereChild() != null && ind.getFamiliesWhereChild().size() == 0) {
                    // root = ind;
                    // System.out.println("GedcomList.main() root = "
                    // + root.getFormattedName());
                }
            }
            System.out.println();
        }
        System.out.println("(" + individuals.size() + " rows)");

        Individual root2 = individuals.get("@" + root + "@");

        String ret = printFamilyOf(root2, "", star);
        System.out.println(ret);
        System.err.println(
                "TIP: Use PlantUML (https://plantuml.com/style-evolution) or pandoc (see tree_rohidekar_master.mwk header for command)");
        System.err.println("GedcomList.main()\n./gedcom2mwk.osx ~/sarnobat.git/genealogy/rohidekar.ged | pandoc --from mediawiki --to html | tee /tmp/index.html");

    }

    private static String printFamilyOf(Individual root, String indentation, String star) {
        List<PersonalName> names = root.getNames();
        String familyStr = indentation + " ";
        if (names != null) {
            if (names.size() > 0) {
                for (PersonalName pn : names) {
                    PersonalName personalName = pn;
                    String first = personalName.getBasic().replace("/", "");
                    familyStr += first;
                }
            }
        }
        String familyStr2 = "";
        List<FamilySpouse> familiesWhereSpouse = root.getFamiliesWhereSpouse();
        if (familiesWhereSpouse != null) {
            for (FamilySpouse familySpouse : familiesWhereSpouse) {
                Family family2 = familySpouse.getFamily();
                IndividualReference spouse = root.equals(family2.getWife()) ? family2.getHusband() : family2.getWife();
                if (spouse != null) {
                    Individual individual = spouse.getIndividual();
                    familyStr += "  " + individual.getFormattedName();
                }
                Family family = family2;
                if (family != null) {
                    if (family.getChildren() != null) {
                        for (IndividualReference ir : family.getChildren()) {
                            // System.out.print("\n");
                            familyStr2 += printFamilyOf(ir.getIndividual(), indentation + star, star);
                        }
//                      familyStr2 += "\n";
                    }
                }
            }
        }
        // System.out.print("\n");
        return familyStr + "\n" + familyStr2;
    }
}
