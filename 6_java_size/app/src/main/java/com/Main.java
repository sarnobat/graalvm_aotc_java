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
import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.StringCharacterIterator;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * find | /Volumes/git/github/graalvm_aotc_java/6_java_size/size.mac.intel 1M 10M
 * find | /Volumes/numerous/usr/local/homebrew/Cellar/openjdk@17/17.0.4.1_1/bin/java -jar /Volumes/git/github/graalvm_aotc_java/6_java_size/app/build/libs/app.jar 1M 10M
 */
public class Main {
	public static void main(String iArgs[]) throws IOException {

		// 4) cli options
		if (System.getProperties().containsKey("-h")) {
			System.out.println(help());
			System.exit(-1);
			throw new RuntimeException("so java compiles");
		}

		long theUpperLimit = getUpper(iArgs);
		long theLowerLimit = getLower(iArgs);

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
						if (aSizeBytes <= theUpperLimit) {
							if (aSizeBytes >= theLowerLimit) {
//								System.err.printf("%10d >= %d\n", aSizeBytes, theLowerLimit);
								System.out.printf("%s\t%s\n", humanReadableByteCountSI(aSizeBytes), aLine);
							}
						}
					} else {

					}
				} else {
					System.err.printf("[missing] %s\n", aLine);
				}

			}
		}
	}

	private static long getLower(String[] iArgs) {
		long oLowerSizeBytes;
//		System.err.println("Main.getLower() iArgs.length\t= " + iArgs.length);
		if (iArgs.length == 0) {
			oLowerSizeBytes = Long.MIN_VALUE;
		} else if (iArgs.length == 1) {
			oLowerSizeBytes = toBytes(iArgs[0]);
		} else if (iArgs.length == 2) {
//			System.err.println("Main.getLower() iArgs\t= " + iArgs[0] + "\t" + iArgs[1]);
			oLowerSizeBytes = toBytes(iArgs[0]);
		} else {
			oLowerSizeBytes = Long.MIN_VALUE;
		}
//		System.err.println("Main.getLower() oLowerSizeBytes = " + oLowerSizeBytes);
		return oLowerSizeBytes;
	}

	private static long toBytes(String string) {
		String numberWithoutSuffixStr;
		long multiple;
		if (string.endsWith("M")) {
			numberWithoutSuffixStr = removeLastChars(string, 1);
			multiple = 2;
		} else if (string.endsWith("K")) {
			numberWithoutSuffixStr = removeLastChars(string, 1);
			multiple = 1;
		} else if (string.endsWith("G")) {
			numberWithoutSuffixStr = removeLastChars(string, 1);
			multiple = 3;
		} else if (string.endsWith("T")) {
			numberWithoutSuffixStr = removeLastChars(string, 1);
			multiple = 4;
		} else {
			numberWithoutSuffixStr = string;
			multiple = 0;
		}
		long numberWithoutSuffix = Long.parseLong(numberWithoutSuffixStr);
//		System.err.printf("Main.toBytes() %d * 1024^%d\n", numberWithoutSuffix, multiple);
		return numberWithoutSuffix * Math.round(Math.pow(1024, multiple));
	}

	private static String removeLastChars(String str, int chars) {
		return str.substring(0, str.length() - chars);
	}

	private static long getUpper(String[] iArgs) {
		long oUpperSizeBytes;
//		System.err.println("Main.getLower() iArgs.length\t= " + iArgs.length);
		if (iArgs.length == 0) {
			oUpperSizeBytes = Long.MAX_VALUE;
		} else if (iArgs.length == 1) {
//			System.err.printf("getUpper() No upper specified");
			oUpperSizeBytes = Long.MAX_VALUE;
		} else if (iArgs.length == 2) {
//			System.err.println("Main.getLower() iArgs\t= " + iArgs[0] + "\t" + iArgs[1]);
			oUpperSizeBytes = toBytes(iArgs[1]);
		} else {
			oUpperSizeBytes = Long.MAX_VALUE;
		}
//		System.err.println("Main.getLower() oLowerSizeBytes = " + oUpperSizeBytes);
		return oUpperSizeBytes;
	}

	@Deprecated
	public static String readableFileSize(long size) {
		if (size <= 0) {
			return "0";
		}
		final String[] units = new String[] { "", "K", "M", "G", "T" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	private static String humanReadableByteCountSI(long bytes) {
		if (-1000 < bytes && bytes < 1000) {
			return bytes + "";
		}
		CharacterIterator ci = new StringCharacterIterator("KMGTPE");
		while (bytes <= -999_950 || bytes >= 999_950) {
			bytes /= 1000;
			ci.next();
		}
		return String.format("%.0f%c", bytes / 1000.0, ci.current());
	}

	@Deprecated
	private static String humanReadableByteCountBin(long bytes) {
		long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
		if (absB < 1024) {
			return bytes + " B";
		}
		long value = absB;
		CharacterIterator ci = new StringCharacterIterator("KMGTPE");
		for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
			value >>= 10;
			ci.next();
		}
		value *= Long.signum(bytes);
		return String.format("%.1f %ciB", value / 1024.0, ci.current());
	}

	@Deprecated
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
		return "usage: size [lower] [upper]?";
	}
}