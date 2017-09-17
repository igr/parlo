package com.oblac.parlo.answer;

import com.oblac.parlo.Sentence;
import com.oblac.parlo.SentenceScore;
import com.oblac.parlo.Story;
import com.oblac.parlo.StoryParser;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Answer finder.
 */
public class AnswerFinder {
	private final StoryParser storyParser;

	private final BagOfWordsMatcher bagOfWordsMatcher = new BagOfWordsMatcher();
	private final TripletsMatcher tripletsMatcher = new TripletsMatcher();
	private final KeywordsMatcher keywordsMatcher = new KeywordsMatcher();
	private final SentenceRanker sentenceRanker = new SentenceRanker();

	public AnswerFinder(StoryParser storyParser) {
		this.storyParser = storyParser;
	}

	/**
	 * Finds answer for the given input on the story. Scans the whole story, sentence by sentence; finds the
	 * sentences that matches the question the most, and returns top 3 results.
	 */
	public void answer(Story story, String input) {
		final List<Sentence> inputSentences = storyParser.parseText(input).sentences();

		final List<SentenceScore> answerSentences = story
			.sentences()
			.stream()
			.map(SentenceScore::new)
			.collect(Collectors.toList());

		inputSentences.forEach(question -> {
			for (SentenceScore sentenceScore : answerSentences) {
				double score = 0.0;

				// totally non-scientific coefficients used here

				//score += tripletsMatcher.matchStoryTriples(sentenceScore.sentence(), question);

				//score += 0.4 * bagOfWordsMatcher.matchBagOfWords(sentenceScore.sentence(), question);

				//score += 2 * keywordsMatcher.match(sentenceScore.sentence(), question);

				score += 2 * sentenceRanker.rankSentence(sentenceScore.sentence(), question);

				sentenceScore.increase(score);
			}
		});

		answerSentences.sort(Comparator.comparingDouble(SentenceScore::score));

		System.out.println("------------------------------- RESULTS");

		answerSentences.subList(answerSentences.size() - 3, answerSentences.size()).forEach(System.out::println);

	}
}