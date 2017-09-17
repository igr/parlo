package com.oblac.parlo.answer;

import weka.core.stemmers.IteratedLovinsStemmer;

import static com.oblac.parlo.answer.LemmaMatcher.Match.*;

/**
 * Lemma matcher, that uses lemma or stemed word for matching.
 */
public class LemmaMatcher {
	enum Match {SAME, ALIKE, DIFFERENT}

	public Match matchLemmas(String lemma1, String lemma2) {
		if (lemma1.equals(lemma2)) {
			return SAME;
		}

		lemma1 = new IteratedLovinsStemmer().stem(lemma1);
		lemma2 = new IteratedLovinsStemmer().stem(lemma2);

		if (lemma1.equals(lemma2)) {
			return ALIKE;
		}

		return DIFFERENT;
	}
}
