package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Unlike some rudimentary regex, we parse the bash script properly so that
 * whitespace inside legitimate commands gets interpreted correctly.
 */
public class GedcomCli {

    private static Map<String, Individual> childToMother = new HashMap<>();
    private static Map<String, Individual> childToFather = new HashMap<>();
    private static Map<Individual, String> individualToChildFamilyId = new HashMap<>();
//    private static Map<String, String> displayNameOfChildToParent = new HashMap<>();
    private static Map<String, Individual> idToIndividual = new HashMap<>();
    private static Map<String, Individual> displayNameToIndividualWithSpouse = new HashMap<>();
    private static Map<String, Marriage> idToFamily = new HashMap<>();
//    private static Set<Individual> individualsWithNoParent = new HashSet<>();
    private static Multimap<String, Individual> displayNameToChildrenWithSpouse = HashMultimap.create();

    private static final String ROOT_ID = "I25";
//  private static final String ROOT_ID = "I44";

    public static void main(String[] args) throws IOException {
//        boolean showSpouses = Boolean.parseBoolean(System.getProperty("spouses", "true"));
        System.err.println("GedcomCli.main() 1");
        if (args.length == 0) {
            printHelp();
//            System.exit(0);
        }

        File myObj = new File(System.getProperty("user.home") + "/sarnobat.git/2021/gedcom/rohidekar.ged");
        Scanner myReader;

        try {
            System.err.println("GedcomCli.main.run() 2");
            myReader = new Scanner(myObj);

            System.err.println("GedcomCli.main.run() 3");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Individual individual = null;
        Marriage family = null;
        System.err.println("GedcomCli.main.run() 4");
        while (myReader.hasNextLine()) {
//                      System.err.println("GedcomCli.main.run() 5");
            String data = myReader.nextLine();
            if (data.startsWith("0") && data.endsWith("INDI")) {

                if (individual != null) {
                    // System.err.println(individual.toString());
                }
                String regex = "0..(.*)..INDI";
                Pattern p = Pattern.compile(regex);
                Matcher matcher = p.matcher(data);
                if (matcher.find()) {
                    String s = matcher.group(1);
                    individual = new Individual(s);
                    idToIndividual.put(s, individual);
                } else {
                    throw new RuntimeException("Developer error for line: " + data);
                }
                continue;
            }
            if (individual == null) {
                continue;
            }
            if (data.startsWith("2 GIVN")) {
                String replaceAll = data.replaceAll(".*GIVN ", "");
                individual.setFirstName(replaceAll);
            } else if (data.startsWith("2 SURN")) {
                String replaceAll = data.replaceAll(".*SURN ", "");
                individual.setLastName(replaceAll);
//                    displayNameToIndividual.put(individual.toString(), individual);
            } else if (data.startsWith("0") && data.endsWith("FAM")) {
                String regex = "0..(.*)..FAM";
                Pattern p = Pattern.compile(regex);
                Matcher matcher = p.matcher(data);
                if (matcher.find()) {
                    String s = matcher.group(1);
                    family = new Marriage(s);
                    idToFamily.put(s, family);
                } else {
                    throw new RuntimeException("Developer error");
                }
            } else if (data.startsWith("1 FAMS")) {
                String replaceAll = data.replaceAll("1 FAMS .", "").replaceAll(".\044", "");
                individualToChildFamilyId.put(individual, replaceAll);
            } else if (data.startsWith("1 HUSB")) {
                String replaceAll = data.replaceAll(".*HUSB .", "").replaceAll(".\044", "");
                Individual husband = idToIndividual.get(replaceAll);
                family.setHusband(husband);
            } else if (data.startsWith("1 WIFE")) {
                String replaceAll = data.replaceAll(".*WIFE .", "").replaceAll(".\044", "");
                family.setWife(idToIndividual.get(replaceAll));
            } else if (data.startsWith("1 CHIL")) {
                String replaceAll = data.replaceAll(".*CHIL .", "").replaceAll(".\044", "");
                Individual i = idToIndividual.get(replaceAll);
                family.addChild(i);
//                i.setParentFamily(family);
            }
        }
        myReader.close();
        if (idToFamily.size() < 88) {
            throw new RuntimeException("missing families");
        }
        if (idToIndividual.size() < 256) {
            throw new RuntimeException("missing individual");
        }
        // if (!idToIndividual.keySet().contains("F10")) {
        // throw new RuntimeException();
        // }
        // attach each individual to its family
        for (Individual i : individualToChildFamilyId.keySet()) {
            Marriage f = idToFamily.get(individualToChildFamilyId.get(i));
            i.setChildFamily(f);
            i.addChildFamily(f);
            // System.err.println("Has parent: " + i.toString());
        }
        for (Marriage f : idToFamily.values()) {
//                      System.out .println("SRIDHAR GedcomCli.main.run() family father = " + f.getHusband().toString() + "\thas " + f.getChildren().size() + " children: " + f.getChildren().toString());
            f.getHusband().setSpouse(f.getWife());
            f.getWife().setSpouse(f.getHusband());
            for (Individual child : f.getChildren()) {

                if ("I119".equals(child.getId())) {

                }
                childToFather.put(child.getId(), f.getHusband());
                childToMother.put(child.getId(), f.getWife());

                displayNameToChildrenWithSpouse.put(f.getHusband().toString(), child);
                displayNameToChildrenWithSpouse.put(f.getWife().toString(), child);

            }
            if (!f.getHusband().toString().contains("--")) {
                System.err.println("[warn] SRIDHAR GedcomCli.run() missing " + f.getHusband().toString()
                        + " . See if showid=true fixes it.");
            }
        }
        for (Marriage f : idToFamily.values()) {
            System.err.println("[debug] f.getHusband().toString() = " + f.getHusband().toString());
            displayNameToIndividualWithSpouse.put(f.getHusband().toString(), f.getHusband());
            displayNameToIndividualWithSpouse.put(f.getWife().toString(), f.getWife());
        }
        for (String id : idToIndividual.keySet()) {
            if (!childToFather.containsKey(id) && !childToMother.containsKey(id)) {
                System.err.println("[debug] " + id + " has no parents :" + idToIndividual.get(id));
            }
        }

        if (displayNameToChildrenWithSpouse.size() < 20) {
            throw new RuntimeException();
        }
        if (!idToIndividual.keySet().contains(ROOT_ID)) {
            throw new RuntimeException("Missing root ID " + ROOT_ID);
        }

        String o = "Venkat Rao Rohidekar I29 -- Tarabai  I30";
        if (!displayNameToIndividualWithSpouse.keySet().contains(o)) {
            throw new RuntimeException("developer error: could not find entry for " + o);
        }

        Individual child = displayNameToIndividualWithSpouse.get(o);
        if (!displayNameToIndividualWithSpouse.containsKey(child.toString())) {
            for (String s : displayNameToIndividualWithSpouse.keySet()) {
                System.err.println("SRIDHAR GedcomCli.main.run() " + s);
            }
            throw new RuntimeException("");
        }
//        System.out.println(printEdges(idToFamily));
        System.out.println(printIndividuals(idToIndividual));
        if (false) {
            switch (args[0]) {
            case "dump":
                System.out.println(printFamiliesRecursive(idToIndividual.get(ROOT_ID).getChildFamily(), ""));
                break;
            case "nodes":
            case "individuals":
                System.out.println(printIndividuals(idToIndividual));
                break;
            case "edges":
                System.out.println(printEdges(idToFamily));
                break;
            case "families":
                System.out.println(printFamilies(idToFamily));
                break;
            default:
                printHelp();
                break;
            }
        }
        // I24 - root

    }

    private static String separator = "\t";
    private static StringBuffer printEdges(Map<String, Marriage> idToFamily3) {
        StringBuffer sb = new StringBuffer();
        for (String id : new TreeSet<>(idToFamily3.keySet())) {
            Marriage f = idToFamily3.get(id);
            for (Individual c : f.getChildren()) {
                sb.append(String.format("%-50s %s %30s\n", c.toString(), separator, f.getCouple()));
            }
        }
        return sb;
    }

    private static StringBuffer printFamilies(Map<String, Marriage> idToFamily) {
        String separator = "\t";
        StringBuffer sb = new StringBuffer();
        for (String id : new TreeSet<>(idToFamily.keySet())) {
            Marriage f = idToFamily.get(id);
            sb.append(f.getId());
            sb.append(separator);

            sb.append(f.getHusband().getId());
            sb.append(separator);
            sb.append(f.getHusband().getFirstName() + " " + f.getHusband().getLastName());
            sb.append("\n");

            sb.append(f.getId());
            sb.append(separator);
            sb.append(f.getWife().getId() + separator + f.getWife().getFirstName() + " " + f.getWife().getLastName());
            sb.append("\n");

            for (Individual c : f.getChildren()) {
                sb.append(f.getId());
                sb.append(separator);
                sb.append(c.getId());
                sb.append(separator);
                sb.append(c.getFirstName() + " " + c.getLastName());
                sb.append("\n");
            }
        }
        return sb;
    }

    private static StringBuffer printIndividuals(Map<String, Individual> idToIndividual) {
        StringBuffer sb = new StringBuffer();
        for (String id : new TreeSet<>(idToIndividual.keySet())) {
            sb.append(id);
            sb.append(separator);
            Individual individual = idToIndividual.get(id);
            sb.append(individual.toStringReadable());
            sb.append("\n"); 
        }
        return sb;
    }

    private static void printHelp() {
        System.out.println("TODO: print options");
    }

    private static String printFamiliesRecursive(Marriage f, String string) {
        if (f == null) {
            return "";
        }
        String s = "";// " " + f.getWife().toString();
        for (Individual individual : f.getChildren()) {
            if (individual == f.getHusband()) {
                throw new RuntimeException("infinite loop");
            }
            s += "\n" + string + individual.toString();
            for (Marriage childFamily : individual.getChildFamilies()) {
                s += printFamiliesRecursive(childFamily, string + "  ");
            }
        }
        return s;
    }

    private static class Marriage {
        private final String id;
        private Individual husband;
        private Individual wife;
        private final Set<Individual> children = new HashSet<>();

        Individual getHusband() {
            return husband;
        }

        public String getCouple() {
            return husband.toString();
        }

        public Collection<Individual> getChildren() {
            return children;
        }

        public String getId() {
            return id;
        }

        public void addChild(Individual i) {
            children.add(i);
        }

        void setHusband(Individual husband) {
            this.husband = husband;
        }

        Individual getWife() {
            return wife;
        }

        void setWife(Individual wife) {
            this.wife = wife;
        }

        Marriage(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            String string = id + "  " + husband.toString() + " -- " + wife.toString();
            if (children.size() > 0) {
                string += " (";
                for (Individual i : children) {
                    string += "," + i.toString();
                }
            }
            string += ")";
            return string;
        }
    }

    private static class Individual {
        private final String id;

        private String firstName;
        // TODO: this should be a collection of child families
        @Deprecated
        private Marriage childFamily;
        private final Map<String, Marriage> childFamilies = new HashMap<>();
//        private Marriage parentFamily;
        private Individual spouse;

        @Deprecated
        Marriage getChildFamily() {
            return childFamily;
        }

        public Iterable<Marriage> getChildFamilies() {
            return childFamilies.values();
        }

        public String getId() {
            return id;
        }

        public void setSpouse(Individual husband) {
            this.spouse = husband;
        }

//        public Individual getSpouse() {
//            return this.spouse;
//        }

        @Deprecated
        void setChildFamily(Marriage childFamily) {
            this.childFamily = childFamily;
        }

        void addChildFamily(Marriage childFamily) {
            this.childFamilies.put(childFamily.getId(), childFamily);
        }

//        Marriage getParentFamily() {
//            return parentFamily;
//        }

//        void setParentFamily(Marriage parentFamily) {
//            this.parentFamily = parentFamily;
//        }

        Individual(String id) {
            this.id = id;
        }

        String getFirstName() {
            return firstName == null ? "" : firstName;
        }

        void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        String getLastName() {
            return lastName == null ? "" : lastName;
        }

        void setLastName(String lastName) {
            this.lastName = lastName;
        }

        String lastName;


        String toStringReadable() {
            String spouseString = spouse == null ? ""
                    : " -- " + spouse.getFirstName() + " " + spouse.getLastName();
            return getFirstName() + " " + getLastName() + " " + spouseString;
        }

        
        @Override
        public String toString() {
            String string = spouse == null ? ""
                    : " -- " + spouse.getFirstName() + " " + spouse.getLastName() + " " + spouse.id;
            return getFirstName() + " " + getLastName() + " " + id + string;
        }
    }
}
