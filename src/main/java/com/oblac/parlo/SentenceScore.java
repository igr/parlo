package com.oblac.parlo;

/**
 * Simple composition of {@link Sentence} and it's weight (or score) that is used for ranking.
 */
public class SentenceScore {

	private final Sentence sentence;
	private double score;

	public SentenceScore(Sentence sentence) {
		this.sentence = sentence;
	}

	public void increase(double delta) {
		score += delta;
	}

	public double score() {
		return score;
	}

	public Sentence sentence() {
		return sentence;
	}

	@Override
	public String toString() {
		return "[" + score + "] " + sentence;
	}

}
