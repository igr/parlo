package com.oblac.parlo;

import com.oblac.parlo.processing.WordUtil;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import jodd.mutable.MutableInteger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Sentence {

	private final Story story;
	private final CoreMap coreMap;
	private final Tree tree;
	private final SemanticGraph semanticGraph;
	private final List<Token> tokens;
	private final Sentiment sentiment;
	private final Collection<RelationTriple> triples;
	private final String text;

	private SentenceType sentenceType;
	private QuestionType questionType;
	private Map<String, Boolean> keywords;

	private Set<String> bagOfWords;

	public Sentence(Story story, CoreMap coreMap) {
		this.story = story;

		this.text = coreMap.get(CoreAnnotations.TextAnnotation.class);

		this.coreMap = coreMap;

		this.tree = coreMap.get(TreeCoreAnnotations.TreeAnnotation.class);

		this.semanticGraph = coreMap.get(SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class);

		Tree tree = coreMap.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);

		int sentimentScore = RNNCoreAnnotations.getPredictedClass(tree);

		this.sentiment = Sentiment.valueOf(sentimentScore);

		this.triples = coreMap.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);

		MutableInteger index = new MutableInteger(0);

		this.tokens = coreMap.get(CoreAnnotations.TokensAnnotation.class).stream()
			.map(coreLabel -> {
				int tokenIndex = index.get();
				index.set(tokenIndex + 1);
				return new Token(this, coreLabel, tokenIndex);
			})
			.filter(token -> !POS.isPunctuation(token))
			.collect(Collectors.toList());

		this.bagOfWords = this.tokens.stream()
			.map(Token::lemma)
			.filter(WordUtil::notStopWord)
			.collect(Collectors.toSet());
	}

	public Tree tree() {
		return tree;
	}

	public String text() {
		return text;
	}

	public Collection<RelationTriple> triples() {
		return triples;
	}

	public SemanticGraph semanticGraph() {
		return semanticGraph;
	}

	public Sentiment sentiment() {
		return sentiment;
	}

	public Set<String> bagOfWords() {
		return bagOfWords;
	}

	public List<Token> tokens() {
		return tokens;
	}

	public SentenceType sentenceType() {
		return sentenceType;
	}

	public QuestionType questionType() {
		return questionType;
	}

	public Map<String, Boolean> keywords() {
		return keywords;
	}

	public String toString() {
		return coreMap.toString();
	}

	public void sentenceType(SentenceType sentenceType) {
		this.sentenceType = sentenceType;
	}

	public void questionType(QuestionType questionType) {
		this.questionType = questionType;
	}

	public boolean isQuestion() {
		return sentenceType == SentenceType.QUESTION;
	}

	public void keywords(Map<String, Boolean> keywords) {
		this.keywords = keywords;
	}
}
