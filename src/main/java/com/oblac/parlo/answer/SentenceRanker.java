package com.oblac.parlo.answer;

import com.oblac.parlo.Sentence;
import com.oblac.parlo.processing.TypeDependencyUtil;
import com.oblac.parlo.processing.WordUtil;

import java.util.Collections;
import java.util.Set;

/**
 * Sentence ranker, matches sentence and the question and calculate the score based on various triplet dependency rules.
 */
public class SentenceRanker {

	public double rankSentence(final Sentence context, final Sentence question) {
		final TypeDependencyUtil.TypeDependencyData questionData = TypeDependencyUtil.getData(question.text());
		final Set<String> questionRelationSynonyms = WordUtil.getVerbSynonyms(questionData);
		final Set<String> questionRelationAntonyms = WordUtil.getVerbAntonyms(questionData);

		final TypeDependencyUtil.TypeDependencyData contextData = TypeDependencyUtil.getData(context.text());
		final Set<String> contextRelationSynonyms = WordUtil.getVerbSynonyms(contextData);
		final Set<String> contextRelationAntonyms = WordUtil.getVerbAntonyms(contextData);

		// Term frequency
		final int rule0 = WordUtil.getLemmaFrequency(context, question);

		// Relation object match
		final int rule1 = questionData.getRelation() != null && questionData.getObject() != null
			&& questionData.getRelation().equalsIgnoreCase(contextData.getRelation())
			&& questionData.getObject().equalsIgnoreCase(contextData.getObject()) ? 3 : 0;

		// Relation object-subject match
		final int rule2 = questionData.getRelation() != null && questionData.getObject() != null
			&& questionData.getRelation().equalsIgnoreCase(contextData.getRelation())
			&& questionData.getObject().equalsIgnoreCase(contextData.getSubject()) ? 3 : 0;

		// Any verb match
		final int rule3 = !Collections.disjoint(WordUtil.getVerbs(context), WordUtil.getVerbs(question)) ? 2 : 0;

		// Relation synonym match
		final int rule4 = questionRelationSynonyms != null && questionRelationSynonyms.contains(contextData.getRelation())
			|| contextRelationSynonyms != null && contextRelationSynonyms.contains(questionData.getRelation()) ? 2 : 0;

		// Relation antonym match
		final int rule5 = questionRelationAntonyms != null && questionRelationAntonyms.contains(contextData.getRelation())
			|| contextRelationAntonyms != null && contextRelationAntonyms.contains(questionData.getRelation()) ? 2 : 0;

		// TODO: only to be used with Who
		//final int ruleX = context.nerTags().contains("PERSON") || context.nerTags().contains("ORGANIZATION") ? 0 : -999;

		// Total score

		return rule0 + rule1 + rule2 + rule3 + rule4 + rule5;
	}

}
