package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.SequenceInputStream;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

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
                String[] csv = new com.opencsv.CSVParser().parseLine(line);
                if (csv.length < 2) {
                    continue;
                }
                String child = csv[0];
                String parent = csv[1];
                map.put(parent, child);
            }
        }

        Set<String> roots = Sets.difference(map.keySet(), new HashSet<>(map.values()));

        StringBuffer sb = new StringBuffer();
        for (String parent : roots) {
            sb.append(printAllPaths("", parent, map));
        }
        // This doesn't work with pipes flushing
        {

//        System.out.println(sb.toString());
        }
        {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(System.out);
            outputStreamWriter.write(sb.toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();
        }
        {
//        try {    
//            BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));
////            System.out.println(sb.toString());
//            log.write(sb.toString());
//            log.flush();
//            log.close();
//          }
//          catch (Exception e) {
//            e.printStackTrace();
//          }
        }
        // This doesn't solve it either
        {
            String[] lines = sb.toString().split("\n");
            for (String s : lines) {
//                System.out.println(s);
            }
        }
        {
            StringWriter sw = null;
            BufferedWriter bw = null;
            
            String str = "Hello World!";
            
            try {
            
               // create string writer
               sw = new StringWriter();
               
               //create buffered writer
               bw = new BufferedWriter(sw);
               
               // writing string to writer
               bw.write(sb.toString());
               
               // forces out the characters to string writer
               bw.flush();
               
               
               //prints the string
               System.out.println(sb);
                  
            } catch(IOException e) {
            
               // if I/O error occurs
               e.printStackTrace();
            } finally {
            
               // releases any system resources associated with the stream
               if(sw!=null)
                  sw.close();
               if(bw!=null)
                  bw.close();
            }
        }
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
//            System.err.println("App.printAllPaths() " + child);
            sb.append(printAllPaths(prefixNew, child, map));
        }
        return sb;
    }

    private static BufferedReader read(String[] args, InputStream in) throws IOException {
        BufferedReader br;
        BufferedReader br1 = new BufferedReader(new InputStreamReader(in));

        if (br1.ready()) {
            br = br1;
            // problem: arg files get ignored
        } else {
//            if (args.length == 0) {
//                System.out.println(help());
//                System.exit(-1);
//                throw new RuntimeException("so java compiles");
//            } else {
            // Check file exists
            SequenceInputStream is = new SequenceInputStream(
                    Collections.enumeration(Arrays.stream(args).filter(f -> Paths.get(f).toFile().exists()).map(f -> {
                        try {
                            // 1) file read
                            return new FileInputStream(f);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList())));
            br = new BufferedReader(new InputStreamReader(is));
//            }
        }
        return br;
    }

    private static String help() {
        String help = "usage: helloworld [ARGS]";
        return help;
    }
}