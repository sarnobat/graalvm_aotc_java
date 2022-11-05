package com;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.net.URI;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * find | /usr/local/Cellar/openjdk@11/11.0.12/bin/java helloworld.java
 */
public class Main {
	public static void main(String args[]) throws IOException {

		// 4) cli options
		if (System.getProperties().containsKey("-h")) {
			System.out.println(help());
			System.exit(-1);
			throw new RuntimeException("so java compiles");
		}
		// 5) map/dictionary/associative array
		Map<String, Integer> map = new HashMap<>();

		/////////////////////////////////////////////////////////////
		// 8) concurrent
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
			boolean createdReport = file.createNewFile();
			System.err.println(dateEpoch);
			try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);) {
				// Read the file using Files.lines and collect it into a
				// List
				map.entrySet().forEach(line -> {
					try {
						writer.write(String.format("%d %s\n", line.getValue(), line.getKey()));
					} catch (IOException e) {
						throw new UncheckedIOException(e);
					}
				});
				writer.flush();
			}

			try (Stream<String> lines = Files.lines(Path.of(path.toUri()))) {
				lines.sorted().forEach(System.out::println);
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