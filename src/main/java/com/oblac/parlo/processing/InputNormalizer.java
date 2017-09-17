package com.oblac.parlo.processing;

import jodd.io.FileUtil;
import jodd.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputNormalizer {

	private static final char[] PUNCTUATIONS = {
		'.', ',', ';', ':', '?', '!',
	};

	public static class StringPair {
		private final String from;
		private final String to;

		public StringPair(String fullLine) {
			String[] pairs = StringUtil.splitc(fullLine, ' ');

			from = StringUtil.replaceChar(pairs[0], '+', ' ');
			to = StringUtil.replaceChar(pairs[1], '+', ' ');
		}

		public String replace(String input) {
			return StringUtil.replace(input, from, to);
		}
	}

	private final List<StringPair> replacements = new ArrayList<>();
	private static final String FOLDER = "data/normalization/";

	public InputNormalizer() {
		try {
			for (String line : FileUtil.readLines(FOLDER + "british.txt")) {
				replacements.add(new StringPair(line));
			}

			for (String line : FileUtil.readLines(FOLDER + "contractions.txt")) {
				replacements.add(new StringPair(line));
			}

			for (String line : FileUtil.readLines(FOLDER + "spelling.txt")) {
				replacements.add(new StringPair(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Loaded normalizeers: " + replacements.size());
	}

	/**
	 * Adds a space before punctuation.
	 */
	public String normalizePunctuations(String sentence) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < sentence.length(); i++) {
			char c = sentence.charAt(i);
			for (char punctuation : PUNCTUATIONS) {
				if (c == punctuation && i > 0) {
					if (sentence.charAt(i - 1) != ' ') {
						sb.append(' ');
					}
				}
			}
			sb.append(c);
		}

		return sb.toString();
	}

	public String normalizeWords(String sentence) {
		String[] split = StringUtil.splitc(sentence, ' ');

		for (int i = 0; i < split.length; i++) {
			String word = split[i];

			for (StringPair stringPair : replacements) {
				word = stringPair.replace(word);
			}

			split[i] = word;
		}

		return StringUtil.join(split, ' ');
	}

}
