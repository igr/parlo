package com.oblac.parlo.answer;

import com.oblac.parlo.Sentence;
import jodd.mutable.MutableInteger;

import java.util.Set;

/**
 * Match bag of words using {@link LemmaMatcher}.
 */
public class BagOfWordsMatcher {

	public int matchBagOfWords(Sentence sentence, Sentence input) {
		final Set<String> inputBagOfWords = input.bagOfWords();

		final Set<String> sentenceBagOfWords = sentence.bagOfWords();
		final LemmaMatcher lemmaMatcher = new LemmaMatcher();

		MutableInteger score = new MutableInteger(0);

		sentenceBagOfWords.forEach(word ->
			inputBagOfWords.forEach(inputWord -> {
				switch (lemmaMatcher.matchLemmas(word, inputWord)) {
					case SAME:
						score.value += 3;
						break;
					case ALIKE:
						score.value += 1;
						break;
				}
			})
		);

		return score.value;
	}

}
