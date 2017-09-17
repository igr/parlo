package com.oblac.parlo.answer;

import com.oblac.parlo.Sentence;
import edu.stanford.nlp.ie.util.RelationTriple;
import jodd.mutable.MutableInteger;

import java.util.Collection;

/**
 * Matches triplets. Compares subjects, relations and objects.
 */
public class TripletsMatcher {

	public static class SubjectMatcher {
		public boolean match(String subject1, String subject2) {
			return subject1.equals(subject2);
		}
	}

	public static class RelationMatcher {
		public boolean match(String relation1, String relation2) {
			return relation1.equals(relation2);
		}
	}

	public static class ObjectMatcher {
		public boolean match(String object1, String object2) {
			return !new LemmaMatcher()
				.matchLemmas(object1, object2)
				.equals(LemmaMatcher.Match.DIFFERENT);
		}
	}

	public int matchStoryTriples(Sentence sentence, Sentence input) {
		final Collection<RelationTriple> inputTriplets = input.triples();

		Collection<RelationTriple> sentenceTriplets = sentence.triples();

		return matchSentenceTriplets(inputTriplets, sentenceTriplets);
	}

	private int matchSentenceTriplets(Collection<RelationTriple> inputTriplets, Collection<RelationTriple> sentenceTriplets) {
		final SubjectMatcher subjectMatcher = new SubjectMatcher();
		final RelationMatcher relationMatcher = new RelationMatcher();
		final ObjectMatcher objectMatcher = new ObjectMatcher();

		MutableInteger score = new MutableInteger(0);

		// find triplet that matches the most

		sentenceTriplets.forEach(triplet -> {
			final String sentenceSubjectLemma = triplet.subjectLemmaGloss();
			final String sentenceRelationLemma = triplet.relationLemmaGloss();
			final String sentenceObjectLemma = triplet.objectLemmaGloss();

			inputTriplets
				.stream()
				.map(inputTriplet -> {
						int count = 0;

						final String inputSubjectLemma = inputTriplet.subjectLemmaGloss();
						final String inputRelationLemma = inputTriplet.relationLemmaGloss();
						final String inputObjectLemma = inputTriplet.objectLemmaGloss();

						if (subjectMatcher.match(sentenceSubjectLemma, inputSubjectLemma)) {
							count += 3;
						}
						if (relationMatcher.match(sentenceRelationLemma, inputRelationLemma)) {
							count += 2;
						}
						if (objectMatcher.match(sentenceObjectLemma, inputObjectLemma)) {
							count += 3;
						}

						return count;
					}
				)
				.max(Integer::compareTo)
				.ifPresent(score::set);
		});

		return score.intValue();
	}

}
