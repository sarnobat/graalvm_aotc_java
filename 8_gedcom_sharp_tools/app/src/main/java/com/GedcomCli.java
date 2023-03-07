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
 * ./gedcom.osx edges  | /Volumes/git/github/graalvm_aotc_java/9_csv2path/csv2path.osx | path2indent.osx --absolute
 */
public class GedcomCli {

    private static Map<String, Individual> childToMother = new HashMap<>();
    private static Map<String, Individual> childToFather = new HashMap<>();
    private static Map<Individual, String> individualToChildFamilyId = new HashMap<>();
    private static Map<String, Individual> idToIndividual = new HashMap<>();
    private static Map<String, Individual> displayNameToIndividualWithSpouse = new HashMap<>();
    private static Map<String, Marriage> idToFamily = new HashMap<>();
    private static Multimap<String, Individual> displayNameToChildrenWithSpouse = HashMultimap.create();

    private static final String ROOT_ID = "I25";

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            printHelp();
            System.exit(0);
        }

        File myObj = new File(System.getProperty("user.home") + "/sarnobat.git/2021/gedcom/rohidekar.ged");
        Scanner myReader;
        try {
            myReader = new Scanner(myObj);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Individual individual = null;
        Marriage family = null;
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            if (data.startsWith("0") && data.endsWith("INDI")) {

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
            }
        }
        myReader.close();
        if (idToFamily.size() < 88) {
            throw new RuntimeException("missing families");
        }
        if (idToIndividual.size() < 256) {
            throw new RuntimeException("missing individual");
        }
        // attach each individual to its family
        for (Individual i : individualToChildFamilyId.keySet()) {
            Marriage f = idToFamily.get(individualToChildFamilyId.get(i));
            i.setChildFamily(f);
            i.addChildFamily(f);
        }
        for (Marriage f : idToFamily.values()) {
            f.getHusband().setSpouse(f.getWife());
            f.getWife().setSpouse(f.getHusband());
            for (Individual child : f.getChildren()) {

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
            displayNameToIndividualWithSpouse.put(f.getHusband().toString(), f.getHusband());
            displayNameToIndividualWithSpouse.put(f.getWife().toString(), f.getWife());
        }

        if (displayNameToChildrenWithSpouse.size() < 20) {
            throw new RuntimeException();
        }
        if (!idToIndividual.keySet().contains(ROOT_ID)) {
            throw new RuntimeException("Missing root ID " + ROOT_ID);
        }

        String o = "Venkat Rao Rohidekar I29 -- Tarabai I30";
        if (!displayNameToIndividualWithSpouse.keySet().contains(o)) {
            throw new RuntimeException("developer error: could not find entry for " + o);
        }

        Individual child = displayNameToIndividualWithSpouse.get(o);
        if (!displayNameToIndividualWithSpouse.containsKey(child.toString())) {
            throw new RuntimeException("");
        }
        if (true) {
            switch (args[0]) {
            case "dump":
                System.out.println(printFamiliesRecursive(idToIndividual.get(ROOT_ID).getChildFamily(), ""));
                break;
            case "ls":
            case "list":
            case "nodes":
            case "individuals":
                System.out.println(printIndividuals(idToIndividual));
                break;
            case "find":
            case "csv":
            case "edges":
                System.out.println(printEdges(idToFamily));
                System.out.flush();
                break;
            case "families":
                System.out.println(printFamilies(idToFamily));
                break;
            default:
                printHelp();
                break;
            }
        }
    }

    private static String separator = "\t";
    private static StringBuffer printEdges(Map<String, Marriage> idToFamily3) {
        StringBuffer sb = new StringBuffer();
        for (String id : new TreeSet<>(idToFamily3.keySet())) {
            Marriage f = idToFamily3.get(id);
            for (Individual c : f.getChildren()) {
                if (c.toString().contains("/")) {
                    System.err.println("[warn] GedcomCli.printEdges(): 1 escape slashes in: " + c.toString());
                }
                if (f.getCouple().contains("/")) {
                    System.err.println("[warn] GedcomCli.printEdges(): 2 escape slashes in: " + f.getCouple());
                }
                sb.append(String.format("%s,%s\n", c.toString().replace("/", "__").stripLeading(), f.getCouple().replace("/", "__").stripLeading()));
                sb.append(String.format("%s,%s\n", c.toString().replace("/", "__").stripLeading(), f.getCoupleReversed().replace("/", "__").stripLeading()));
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
        String s = "";
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
        
        public String getCoupleReversed() {
            return wife.toString();
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
            return string.trim();
        }
    }

    private static class Individual {
        private final String id;

        private String firstName;
        // TODO: this should be a collection of child families
        @Deprecated
        private Marriage childFamily;
        private final Map<String, Marriage> childFamilies = new HashMap<>();
        private Individual spouse;

        @Deprecated
        Marriage getChildFamily() {
            return childFamily;
        }

        public Iterable<Marriage> getChildFamilies() {
            return childFamilies.values();
        }

        public String getId() {
            return id.trim();
        }

        public void setSpouse(Individual husband) {
            this.spouse = husband;
        }

        @Deprecated
        void setChildFamily(Marriage childFamily) {
            this.childFamily = childFamily;
        }

        void addChildFamily(Marriage childFamily) {
            this.childFamilies.put(childFamily.getId(), childFamily);
        }

        Individual(String id) {
            this.id = id.trim();
        }

        String getFirstName() {
            return firstName == null ? "" : firstName.trim();
        }

        void setFirstName(String firstName) {
            this.firstName = firstName.trim();
        }

        String getLastName() {
            return lastName == null ? "" : lastName.trim();
        }

        void setLastName(String lastName) {
            this.lastName = lastName.trim();
        }

        String lastName;

        String toStringReadable() {
            String spouseString = spouse == null ? ""
                    : " -- " + spouse.getFirstName() + " " + spouse.getLastName();
            return (getFirstName() + " " + getLastName() + " " + spouseString).trim().replaceAll("\\s+", " ");
        }

        
        @Override
        public String toString() {
            String string = spouse == null ? ""
                    : " -- " + spouse.getFirstName() + " " + spouse.getLastName() + " " + spouse.id;
            return (getFirstName() + " " + getLastName() + " " + id + string).trim().replaceAll("\\s+", " ");
        }
    }
}
