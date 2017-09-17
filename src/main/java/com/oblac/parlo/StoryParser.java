package com.oblac.parlo;

import com.oblac.parlo.classifier.question.QuestionClassifier;
import com.oblac.parlo.classifier.sentence.SentenceClassifier;
import com.oblac.parlo.processing.KeywordExtractor;

import java.util.Map;

/**
 * Story parser.
 */
public class StoryParser {

	private final CoreNLPEngine coreNLPEngine;
	private final QuestionClassifier questionClassifier;
	private final SentenceClassifier sentenceClassifier;
	private final KeywordExtractor keywordExtractor;

	public StoryParser() {
		this(true);
	}
	public StoryParser(boolean trainMe) {
		this.coreNLPEngine = new CoreNLPEngine(CoreNLP.createStanfordCoreNLP());
		this.sentenceClassifier = new SentenceClassifier();
		this.questionClassifier = new QuestionClassifier();
		this.keywordExtractor = new KeywordExtractor();

		if (trainMe) {
			sentenceClassifier.trainBig();
			questionClassifier.train();
		}
	}

	/**
	 * Parses and classifies text.
	 */
	public Story parseText(String text) {
		Story story = new Story(text, coreNLPEngine.annotate(text));

		story.sentences().forEach(sentence -> {
			SentenceType sentenceType = SentenceType.UNCLASSIFIED;
			try {
				sentenceType = sentenceClassifier.classifySentence(sentence);
			}
			catch (Exception ignore) {}
			sentence.sentenceType(sentenceType);
		});
		story.sentences().forEach(sentence -> {
			QuestionType questionType = QuestionType.NA;
			try {
				questionType = questionClassifier.classifyQuestion(sentence);
			}
			catch (Exception ignore) {}
			sentence.questionType(questionType);
		});
		story.sentences().forEach(sentence -> {
			Map<String, Double> keywords = keywordExtractor.keywordsExtractor(sentence, sentence.isQuestion());
			sentence.keywords(keywords);
		});

		return story;
	}
}
