package com.oblac.parlo;

/**
 * Sentiment classification of the sentence.
 */
public enum Sentiment {

	VERY_NEGATIVE(0),
	NEGATIVE(1),
	NEUTRAL(2),
	POSITIVE(3),
	VERY_POSITIVE(4);

	Sentiment(int sentimentValue) {
		this.sentimentValue = sentimentValue;
	}

	private int sentimentValue;

	public boolean isMorePositiveThan(Sentiment otherSentiment) {
		return this.sentimentValue > otherSentiment.sentimentValue;
	}

	public boolean isMoreNegativeThan(Sentiment otherSentiment) {
		return this.sentimentValue < otherSentiment.sentimentValue;
	}

	public static Sentiment valueOf(int i) {
		switch (i) {
			case 0: return VERY_NEGATIVE;
			case 1: return NEGATIVE;
			case 2: return NEUTRAL;
			case 3: return POSITIVE;
			case 4: return VERY_POSITIVE;
			default:
				throw new IllegalArgumentException("Unknown sentiment value");
		}
	}
}
