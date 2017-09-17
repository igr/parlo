package com.oblac.parlo;

/**
 * Question classification type, according to TREC.
 */
public enum QuestionType {
	ABBR,
	DESC,
	ENTY,
	HUM,
	LOC,
	NUM,
	NA;             // n/a

	public static QuestionType valueOf(double match) {
		switch ((int) match) {
			case 0:
				return ABBR;
			case 1:
				return DESC;
			case 2:
				return ENTY;
			case 3:
				return HUM;
			case 4:
				return LOC;
			case 5:
				return NUM;
			default:
				return NA;
		}
	}
}
