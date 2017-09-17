package com.oblac.parlo;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * List of POS labels.
 */
public class POS {

	public static String[] WWORDS = {"WDT","WP","WP$","WRB"};
	public static String[] NOUNS = {"NN","NNP","NNPS","NNS"};
	public static String[] VERBS = {"VB","VBD","VBG","VBN", "VBP", "VBZ"};
	public static String[] ADJECTIVES = {"JJ","JJR","JJS"};

	public static String[] LIST = {
		"CC",    // Coordinating conjunction
		"CD",    // Cardinal number
		"DT",    // Determiner
		"EX",    // Existential there
		"FW",    // Foreign word
		"IN",    // Preposition or subordinating conjunction
		"JJ",    // Adjective
		"JJR",    // Adjective, comparative
		"JJS",    // Adjective, superlative
		"LS",    // List item marker
		"MD",    // Modal
		"NN",    // Noun, singular or mass
		"NNS",    // Noun, plural
		"NNP",    // Proper noun, singular
		"NNPS",    // Proper noun, plural
		"PDT",    // Predeterminer
		"POS",    // Possessive ending
		"PRP",    // Personal pronoun
		"PRP$",    // Possessive pronoun
		"RB",    // Adverb
		"RBR",    // Adverb, comparative
		"RBS",    // Adverb, superlative
		"RP",    // Particle
		"SYM",    // Symbol
		"TO",    // to
		"UH",    // Interjection
		"VB",    // Verb, base form
		"VBD",    // Verb, past tense
		"VBG",    // Verb, gerund or present participle
		"VBN",    // Verb, past participle
		"VBP",    // Verb, non­3rd person singular present
		"VBZ",    // Verb, 3rd person singular present
		"WDT",    // Wh­determiner
		"WP",    // Wh­pronoun
		"WP$",    // Possessive wh­pronoun
		"WRB",    // Wh­adverb
	};

	public static String buildPosString() {
		return Arrays
			.stream(LIST)
			.collect(Collectors.joining(","));
	}

	public static boolean isPunctuation(Token token) {
		return token.partOfSpeech().equals(".");
	}

	public static boolean isNoun(Token token) {
		return token.partOfSpeech().startsWith("NN");
	}

	public static boolean isVerb(Token token) {
		return token.partOfSpeech().startsWith("VB");
	}

	public static boolean isAdjective(Token token) {
		return token.partOfSpeech().startsWith("JJ");
	}

	public static boolean isWhWord(Token token) {
		return token.partOfSpeech().startsWith("W");
	}
}
