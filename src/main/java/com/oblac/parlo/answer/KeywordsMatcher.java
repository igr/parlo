package com.oblac.parlo.answer;

import com.oblac.parlo.Sentence;
import com.oblac.parlo.processing.KeywordsMatchingCalculator;
import com.oblac.parlo.processing.WordDistanceCalculator;

import java.util.Map;

public class KeywordsMatcher {

	private KeywordsMatchingCalculator kmc = new KeywordsMatchingCalculator(new WordDistanceCalculator());

	public double match(Sentence sentence, Sentence question) {
		Map<String, Boolean> questionKeywords = question.keywords();

		Map<String, Boolean> sentenceKeywords = sentence.keywords();

		return kmc.matcherKeywords(sentenceKeywords, questionKeywords);
	}

}
