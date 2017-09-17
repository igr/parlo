package com.oblac.parlo.processing;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

public class WordDistanceCalculator {

	private final ILexicalDatabase db;

	public WordDistanceCalculator() {
		this.db = new NictWordNet();
	}


	public double wordDistance(String word1, String word2) {
		WS4JConfiguration.getInstance().setMFS(true);

		return new Path(db).calcRelatednessOfWords(word1, word2);
	}


}
