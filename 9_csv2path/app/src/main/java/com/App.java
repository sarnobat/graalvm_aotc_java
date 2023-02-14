package com;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 */
public class App {
    public static void main(String args[]) throws IOException {

        if (System.getProperties().containsKey("-h")) {
            System.out.println(help());
            System.exit(-1);
            throw new RuntimeException("so java compiles");
        }
        Multimap<String, String> map = HashMultimap.create();

        try (BufferedReader bufferedReader = read(args, System.in)) {
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                System.out.println(line);
                String[] csv = new com.opencsv.CSVParser().parseLine(line);
                String child = csv[0];
                String parent = csv[1];
                map.put(parent, child);
            }
        }

    }

    private static BufferedReader read(String[] args, InputStream in) throws IOException {
        BufferedReader br;
        BufferedReader br1 = new BufferedReader(new InputStreamReader(in));

        if (br1.ready()) {
            br = br1;
            // problem: arg files get ignored
        } else {
            if (args.length == 0) {
                System.out.println(help());
                System.exit(-1);
                throw new RuntimeException("so java compiles");
            } else {
                // Check file exists
                SequenceInputStream is = new SequenceInputStream(Collections
                        .enumeration(Arrays.stream(args).filter(f -> Paths.get(f).toFile().exists()).map(f -> {
                            try {
                                // 1) file read
                                return new FileInputStream(f);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }).collect(Collectors.toList())));
                br = new BufferedReader(new InputStreamReader(is));
            }
        }
        return br;
    }

    private static String help() {
        String help = "usage: helloworld [ARGS]";
        return help;
    }
}