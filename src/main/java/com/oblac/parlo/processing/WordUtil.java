package com.oblac.parlo.processing;

import com.oblac.parlo.Sentence;
import com.oblac.parlo.Token;
import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.ws4j.WS4J;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WordUtil {
	private static final List<String> STOP_WORDS = Arrays.asList("a", "about", "above", "above", "across", "after",
		"afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also", "although", "always",
		"am", "among", "amongst", "amoungst", "amount", "an", "and", "another", "any", "anyhow", "anyone", "anything",
		"anyway", "anywhere", "are", "around", "as", "at", "back", "be", "became", "because", "become", "becomes",
		"becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between",
		"beyond", "bill", "both", "bottom", "but", "by", "call", "can", "cannot", "cant", "co", "con", "could",
		"couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight",
		"either", "eleven", "else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone",
		"everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five",
		"for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give",
		"go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein",
		"hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in",
		"inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly",
		"least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more",
		"moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never",
		"nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere",
		"of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our",
		"ours", "ourselves", "out", "over", "own", "part", "per", "perhaps", "please", "put", "rather", "re",
		"same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side",
		"since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime",
		"sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them",
		"themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon",
		"these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout",
		"thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un",
		"under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever",
		"when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon",
		"wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why",
		"will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves",
		"the");


	public static int getLemmaFrequency(final Sentence target, final Sentence source) {
		int frequency = 0;
		for (final String targetLemma : target.tokens().stream().map(Token::lemma).collect(Collectors.toList())) {
			if (STOP_WORDS.contains(targetLemma)) {
				continue;
			}
			for (final String sourceLemma : source.tokens().stream().map(Token::lemma).collect(Collectors.toList())) {
				if (!STOP_WORDS.contains(sourceLemma) && getRootLemma(targetLemma).equals(getRootLemma(sourceLemma))) {
					frequency++;
				}
			}
		}
		return frequency;
	}

	public static double getTripleMatchMultiplier(final Sentence target, final Sentence source) {
		final TypeDependencyUtil.TypeDependencyData targetTriple = TypeDependencyUtil.getData(target.text());
		final TypeDependencyUtil.TypeDependencyData sourceTriple = TypeDependencyUtil.getData(source.text());

		double multiplier = 1.0;

		if (targetTriple.getSubject() != null && sourceTriple.getSubject() != null && targetTriple.getSubject().equals(sourceTriple.getSubject())) {
			multiplier += 0.66;
		}

		if (targetTriple.getRelation() != null && sourceTriple.getRelation() != null && targetTriple.getRelation().equals(sourceTriple.getRelation())) {
			multiplier += 0.66;
		}

		if (targetTriple.getObject() != null && sourceTriple.getObject() != null && targetTriple.getObject().equals(sourceTriple.getObject())) {
			multiplier += 0.66;
		}
		return multiplier;
	}

	public static List<String> getVerbs(final Sentence sentence) {
		final List<String> verbs = new ArrayList<>();

		for (Token token : sentence.tokens()) {
			if (token.pos().startsWith("V")) {
				verbs.add(getRootLemma(token.word()));
			}
		}

		return verbs;
	}

	public static Set<String> getVerbSynonyms(final TypeDependencyUtil.TypeDependencyData data) {
		if (data.getRelation() == null) {
			return null;
		}
		return WS4J.findSynonyms(data.getRelation(), POS.v);
	}
	public static Set<String> getVerbAntonyms(final TypeDependencyUtil.TypeDependencyData data) {
		if (data.getRelation() == null) {
			return null;
		}
		return WS4J.findSeeAntonyms(data.getRelation(), POS.v);
	}

	public static String getRootLemma(final String word) {
		final String lemma = new edu.stanford.nlp.simple.Sentence(word).lemmas().get(0);
		if (lemma.equals(word)) {
			return word;
		}
		return getRootLemma(lemma);
	}

	public static boolean notStopWord(String word) {
		return !STOP_WORDS.contains(word);
	}
}
