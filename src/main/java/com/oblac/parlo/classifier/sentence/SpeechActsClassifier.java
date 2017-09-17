package com.oblac.parlo.classifier.sentence;

import com.oblac.parlo.POS;
import com.oblac.parlo.Sentence;

class SpeechActsClassifier {

	static class Features {
		private final Sentence sentence;
		private final int sentenceLength;
		private final int numberOfNouns;
		private final boolean endingInNounOrAdjective;
		private final boolean beginningInVerb;
		private final int countOfWhMarkers;

		Features(
				Sentence sentence,
				int sentenceLength,
				int numberOfNouns, boolean endingInNounOrAdjective, boolean beginningInVerb, int countOfWhMarkers) {
			this.sentence = sentence;
			this.sentenceLength = sentenceLength;
			this.numberOfNouns = numberOfNouns;
			this.endingInNounOrAdjective = endingInNounOrAdjective;
			this.beginningInVerb = beginningInVerb;
			this.countOfWhMarkers = countOfWhMarkers;
		}

		public Sentence getSentence() {
			return sentence;
		}

		public int getSentenceLength() {
			return sentenceLength;
		}

		public int getNumberOfNouns() {
			return numberOfNouns;
		}

		public boolean isEndingInNounOrAdjective() {
			return endingInNounOrAdjective;
		}

		public boolean isBeginningInVerb() {
			return beginningInVerb;
		}

		public int getCountOfWhMarkers() {
			return countOfWhMarkers;
		}

		@Override
		public String toString() {
			return sentenceLength + "," +
				numberOfNouns + "," +
				(endingInNounOrAdjective ? 1 : 0) + "," +
				(beginningInVerb ? 1 : 0) + ","
				+ countOfWhMarkers;
		}
	}

	public Features classifyFeatures(Sentence sentence) {
		return new Features(
			sentence,
			sentence.tokens().size(),
			(int) sentence.tokens().stream().filter(POS::isNoun).count(),
			sentence.tokens().stream().reduce((first, second) -> second).map(token -> POS.isNoun(token) || POS.isAdjective(token)).get(),
			sentence.tokens().stream().findFirst().map(POS::isVerb).get(),
			(int) sentence.tokens().stream().filter(POS::isWhWord).count()
		);
	}
}
