package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * find | /Volumes/git/github/graalvm_aotc_java/6_java_size/size.mac.intel 
 * find | /Volumes/numerous/usr/local/homebrew/Cellar/openjdk@17/17.0.4.1_1/bin/java -jar /Volumes/git/github/graalvm_aotc_java/6_java_size/app/build/libs/app.jar
 */
public class Main {
	public static void main(String iArgs[]) throws IOException {

		// 4) cli options
		if (System.getProperties().containsKey("-h")) {
			System.out.println(help());
			System.exit(-1);
			throw new RuntimeException("so java compiles");
		}

		/////////////////////////////////////////////////////////////
		// 8) concurrent
		try (BufferedReader theBufferedReader = readStdin(iArgs, System.in)) {

			// 1) stdin loop (with optional file arg)
			for (String aLine = theBufferedReader.readLine(); aLine != null; aLine = theBufferedReader.readLine()) {
				Path aPath = Paths.get(aLine);
				File aFile = aPath.toFile();
				if (aFile.exists()) {
				if (aFile.isFile()) {
					long aSizeBytes = Files.size(aPath);
					System.out.printf("%s\t%s\n", toNumInUnits(aSizeBytes), aLine);
				} else {

				}
				} else {
					System.err.printf("[missing] %s\n", aLine);
				}

			}
		}
	}

	private static String toNumInUnits(long iBytes) {
		int theUnitIndex = 0;
		for (; iBytes > 1024 * 1024; iBytes >>= 10) {
			theUnitIndex++;
		}
		if (iBytes > 1024) {
			theUnitIndex++;
		}
		return String.format("%.0f%c", iBytes / 1024f, " KMGTPE".charAt(theUnitIndex));
	}

	private static BufferedReader readStdin(String[] iArgs, InputStream iInputStream) throws IOException {
		BufferedReader oBufferedReader;
		BufferedReader theBufferedReader = new BufferedReader(new InputStreamReader(iInputStream));

		if (theBufferedReader.ready()) {
			oBufferedReader = theBufferedReader;
			// problem: arg files get ignored
		} else {
			if (iArgs.length == 0) {
				System.out.println(help());
				System.exit(-1);
				throw new RuntimeException("so java compiles");
			} else {
				// Check file exists
				SequenceInputStream theInputStream = new SequenceInputStream(Collections
						.enumeration(Arrays.stream(iArgs).filter(f -> Paths.get(f).toFile().exists()).map(f -> {
							try {
								return new FileInputStream(f);
							} catch (FileNotFoundException anException) {
								throw new RuntimeException(anException);
							}
						}).collect(Collectors.toList())));
				oBufferedReader = new BufferedReader(new InputStreamReader(theInputStream));
			}
		}
		return oBufferedReader;
	}

	private static String help() {
		return "usage: size [ARGS]";
	}
}