package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 */
public class App {
    public static void main(String args[]) throws IOException {

        // 4) cli options
        if (System.getProperties().containsKey("-h")) {
            System.out.println(help());
            System.exit(-1);
            throw new RuntimeException("so java compiles");
        }
        // 5) map/dictionary/associative array
        Map<String, Integer> map = new HashMap<>();

        try (BufferedReader bufferedReader = read(args, System.in)) {

            // 1) stdin loop (with optional file arg)
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                System.out.println(line);

                // 2) regex capture groups
                Pattern pattern = Pattern.compile("^(/.*)/([^\\.]*\\.([^$]*))$");
                Matcher matcher = pattern.matcher(line);
                String dir = matcher.group(1);
                String file = matcher.group(2);
                String extension = matcher.group(3);

                // 5) Read and write to a map
                int count = map.getOrDefault(extension, 0);
                map.putIfAbsent(extension, count++);

                System.out.printf("%s %s %s", extension, file, dir);

                // 11) create json object - not possible without a third party
                // library

                // 10) web scrape - hmmmmm, don't do this in the main loop, keep
                // the input to something easy like file paths, not web links
                // 6) embed shell code inside high level language connecting pipes
                // 9) print with padding
                // 10) write to file

            }
        }
        /////////////////////////////////////////////////////////////

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