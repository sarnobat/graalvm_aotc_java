package com;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.net.URI;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;

/**
 * find | /Volumes/git/github/graalvm_aotc_java/6_java_size/size.mac.intel
 */
public class Main {
	public static void main(String args[]) throws IOException {

		// 4) cli options
		if (System.getProperties().containsKey("-h")) {
			System.out.println(help());
			System.exit(-1);
			throw new RuntimeException("so java compiles");
		}

		/////////////////////////////////////////////////////////////
		// 8) concurrent
		try (BufferedReader bufferedReader = read(args, System.in)) {

			// 1) stdin loop (with optional file arg)
			for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				System.out.println(line);

				// 5) Read and write to a map

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

		// 10) print current date
		// 7) convert epoch to date and vv
		long currentTime = System.currentTimeMillis();
		String dateFormattedString = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.ofEpochDay(currentTime));
		System.out.println(dateFormattedString);
		long dateEpoch = LocalDate.parse(dateFormattedString, DateTimeFormatter.ofPattern("yyyy-MM-DD")).toEpochDay();

		Path path = Paths.get("/tmp/statistics " + dateFormattedString + ".txt");
		// 3) parse file path
		if (path.getParent().toFile().exists()) {

			File file = Files.createFile(path).toFile();
			System.err.println(dateEpoch);
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