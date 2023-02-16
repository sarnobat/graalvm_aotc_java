package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class App {
    public static void main(String args[]) throws IOException {

        if (System.getProperties().containsKey("-h")) {
            System.err.println(help());
            System.exit(-1);
            throw new RuntimeException("so java compiles");
        }
        Multimap<String, String> map = HashMultimap.create();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                String[] csv = new com.opencsv.CSVParser().parseLine(line);
                if (csv.length < 2) {
                    continue;
                }
                String child = csv[0];
                String parent = csv[1];
                map.put(parent, child);
            }
        } catch (Exception e) {
            System.err.println("App.main() " + e.getStackTrace());
            System.exit(-1);

        }

        Set<String> roots = Sets.difference(map.keySet(), new HashSet<>(map.values()));

        StringBuffer sb = new StringBuffer();
        for (String parent : roots) {
            sb.append(printAllPaths("", parent, map));
        }
        System.out.println(sb.toString());
    }

    private static StringBuffer printAllPaths(String prefix, String parent, Multimap<String, String> map) {
        StringBuffer sbPrefix = new StringBuffer();
        sbPrefix.append(prefix);
        sbPrefix.append("/");
        sbPrefix.append(parent);
        sbPrefix.toString();
        String prefixNew = sbPrefix.toString();

        StringBuffer sb = new StringBuffer();
        sb.append(prefixNew);
        sb.append("\n");
        for (String child : map.get(parent)) {
            sb.append(printAllPaths(prefixNew, child, map));
        }
        return sb;
    }

    private static String help() {
        String help = "usage: helloworld [ARGS]";
        return help;
    }
}