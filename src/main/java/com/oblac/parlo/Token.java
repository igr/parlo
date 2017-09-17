package com.oblac.parlo;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.TypedDependency;
import weka.core.stemmers.IteratedLovinsStemmer;

import java.util.Collection;

/**
 * Token represents single word, a part of the {@link Sentence}.
 */
public class Token {

	private final Sentence sentence;
	private final CoreLabel coreLabel;
	private final int index;
	private final String word;
	private final String partOfSpeech;
	private final String namedEntity;
	private final String lemma;
	private final TypedDependency typeDependency;
	private final String stem;
	private final Integer characterOffsetBegin;
	private final Integer characterOffsetEnd;

	public Token(Sentence sentence, CoreLabel coreLabel, int tokenIndex) {
		this.sentence = sentence;
		this.coreLabel = coreLabel;
		this.index = tokenIndex;

		// pure word
		this.word = coreLabel.get(CoreAnnotations.TextAnnotation.class);

		// this is the POS tag of the token
		this.partOfSpeech = coreLabel.get(CoreAnnotations.PartOfSpeechAnnotation.class);

		// this is the NE label of the token
		this.namedEntity = coreLabel.get(CoreAnnotations.NamedEntityTagAnnotation.class);

		// lema
		this.lemma = coreLabel.get(CoreAnnotations.LemmaAnnotation.class);

		this.characterOffsetBegin = coreLabel.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
		this.characterOffsetEnd = coreLabel.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);

		// resolve edge node in semantic graph for this label
		this.typeDependency = findTypeDependency(sentence.semanticGraph(), coreLabel);

		// stemmer
		this.stem = new IteratedLovinsStemmer().stem(word);
	}

	/**
	 * Finds type dependencies in semantics graph.
	 */
	private TypedDependency findTypeDependency(SemanticGraph semanticGraph, CoreLabel coreLabel) {
		Collection<TypedDependency> deps = semanticGraph.typedDependencies();

		for (TypedDependency dependency : deps) {
			if (dependency.dep().backingLabel() == coreLabel) {
				return dependency;
			}
		}

		//throw new RuntimeException("TypeDependency not found");
		return null;
	}

	// ---------------------------------------------------------------- getters

	public Sentence sentence() {
		return sentence;
	}

	/**
	 * Returns the index of this token in the sentence.
	 */
	public int index() {
		return index;
	}

	/**
	 * Returns {@code true} if {@link #typedDependency()} has ROOT as a parent.
	 */
	public boolean hasRootAsDependency() {
		return typeDependency.gov().value().equals("ROOT");
	}

	/**
	 * Returns the token word.
	 */
	public String word() {
		return word;
	}

	/**
	 * Returns POS classification of the word.
	 * @see POS
	 */
	public String partOfSpeech() {
		return partOfSpeech;
	}
	public String pos() {
		return partOfSpeech;
	}

	/**
	 * Returns named entity, if recognized. Returns "O" (as in "Other") when
	 * named entity is not recognized.
	 */
	public String namedEntity() {
		return namedEntity;
	}
	public String ne() {
		return namedEntity;
	}

	/**
	 * Returns lemma of the word.
	 * @see #stem()
	 */
	public String lemma() {
		return lemma;
	}

	/**
	 * REturns stemmed word.
	 * @see #lemma()
	 */
	public String stem() {
		return stem;
	}

	/**
	 * Returns type dependency in the sentence.
	 */
	public TypedDependency typedDependency() {
		return typeDependency;
	}

	public Integer characterOffsetBegin() {
		return characterOffsetBegin;
	}
	public Integer characterOffsetEnd() {
		return characterOffsetEnd;
	}

	@Override
	public String toString() {
		return coreLabel.toString();
	}
}
