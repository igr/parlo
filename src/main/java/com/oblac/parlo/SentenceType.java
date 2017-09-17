package com.oblac.parlo;

public enum SentenceType {
	ASSERTION,
	QUESTION,
	EXPRESSIVE,
	UNCLASSIFIED;

	public static SentenceType valueOf(double match) {
		switch ((int) match) {
			case 0:
				return ASSERTION;
			case 1:
				return QUESTION;
			case 2:
				return EXPRESSIVE;
			default:
				return UNCLASSIFIED;
		}
	}
}
