package com.oblac.parlo.processing;

import java.util.Map;

public class KeywordsMatchingCalculator {

	private final WordDistanceCalculator wdc;

	public KeywordsMatchingCalculator(WordDistanceCalculator wordDistanceCalculator) {
		this.wdc = wordDistanceCalculator;
	}

	public double matcherKeywords(Map<String, Boolean> sentenceKey, Map<String, Boolean> questionKeywords) {
		double count = 0;
		double sum = 0;
		for (String word1 : questionKeywords.keySet()) {
			double maxVal = 0;
			String maxWord = null;
			for (String word2 : sentenceKey.keySet()) {
				double mult = 1.0;
				if (questionKeywords.get(word1)) {
					mult = 2.0;
				}
				if (!word1.equals(word2)) {
					double sim = wdc.wordDistance(word1, word2) * mult;
					sum += sim;
					if (maxWord == null) {
						maxVal = sim;
						maxWord = word2;
					} else if (maxVal < sim) {
						maxVal = sim;
						maxWord = word2;
					}
				} else {
					maxVal = 1.0 * mult;
					maxWord = word2;
					sum += 1.0 * mult;
					break;
				}
			}
			count += maxVal;
		}

		return count;
	}

}
