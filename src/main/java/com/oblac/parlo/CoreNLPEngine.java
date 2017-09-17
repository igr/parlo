package com.oblac.parlo;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

/**
 * Annotation engine. Creates annotations on given text using CoreNLP instance.
 */
public class CoreNLPEngine {

	private final StanfordCoreNLP stanfordCoreNLP;

	public CoreNLPEngine(StanfordCoreNLP stanfordCoreNLP) {
		this.stanfordCoreNLP = stanfordCoreNLP;
	}

	/**
	 * Annotates text.
	 */
	public Annotation annotate(String text) {
		// create an empty Annotation with just the input
		Annotation document = new Annotation(text);

		// run all Annotators on this block text
		annotate(this.stanfordCoreNLP, document);

		return document;
	}

	protected void annotate(StanfordCoreNLP pipeline, Annotation ann) {
		if (ann.get(CoreAnnotations.SentencesAnnotation.class) == null) {
			pipeline.annotate(ann);
		}
		else {
			if (ann.get(CoreAnnotations.SentencesAnnotation.class).size() == 1) {
				CoreMap sentence = ann.get(CoreAnnotations.SentencesAnnotation.class).get(0);

				for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
					token.remove(NaturalLogicAnnotations.OperatorAnnotation.class);
					token.remove(NaturalLogicAnnotations.PolarityAnnotation.class);
				}

				sentence.remove(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
				sentence.remove(NaturalLogicAnnotations.EntailedSentencesAnnotation.class);
				sentence.remove(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
				sentence.remove(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class);
				sentence.remove(SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class);

				pipeline.annotate(ann);
			}
		}
	}
}