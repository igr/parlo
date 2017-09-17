package com.oblac.parlo;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Story {

	private final List<Sentence> sentences;
	private final Map<Integer, CorefChain> corefChainMap;
	private final String text;

	public Story(String text, Annotation document) {
		this.text = text;
		sentences = document.get(CoreAnnotations.SentencesAnnotation.class)
			.stream()
			.map(coreMap -> new Sentence(this, coreMap))
			.collect(Collectors.toList());
		corefChainMap = document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
	}

	public List<Sentence> sentences() {
		return sentences;
	}

	public String text() {
		return text;
	}

	public Map<Integer, CorefChain> corefChainMap() {
		return corefChainMap;
	}
}
