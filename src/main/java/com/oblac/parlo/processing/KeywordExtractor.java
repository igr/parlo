package com.oblac.parlo.processing;

import com.oblac.parlo.Sentence;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordExtractor {

	public Map<String, Double> keywordsExtractor(Sentence sentence, boolean isQuestion) {
		Map<String, Double> keywords = new HashMap<>();

		SemanticGraph dependencies = sentence.semanticGraph();
		List<IndexedWord> questionNodes = dependencies.getAllNodesByPartOfSpeechPattern("WP|WRB|WDT|WRB|WP\\$");
		List<String> answerNodes = new ArrayList<>();
		if (questionNodes.size() > 0 && isQuestion) {
			for (SemanticGraphEdge edge : dependencies.edgeListSorted()) {
				IndexedWord dep = edge.getDependent();
				IndexedWord gov = edge.getGovernor();
				if (questionNodes.contains(dep)) {
					answerNodes.add(gov.word());
				} else if (questionNodes.contains(gov)) {
					answerNodes.add(dep.word());    // Identify the most important word in the question
				}
			}
		}

		sentence.tokens().forEach(token -> {
			String word = token.word();
			String ne = token.ne();
			String pos = token.pos();
			String lemma = token.lemma();
			if (!ne.equals("O") || pos.matches("JJ|JJR|JJS|RB|RBR|RBS|NN|NNS|NNP|NNPS|VB|VBD|VBG|VBN|VBP|VBZ|CD") || answerNodes.contains(word)) {
				if (answerNodes.contains(word)) {
					keywords.put(lemma, 1.0);
				} else {
					keywords.put(lemma, 0.0);
				}
			}
		});

		return keywords;
	}

}
