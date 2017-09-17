package com.oblac.parlo.classifier.question;

import com.oblac.parlo.POS;
import com.oblac.parlo.Sentence;
import com.oblac.parlo.Token;

import java.util.List;

class QuestionTypeClassifier {

	static class Features {
		private final Sentence sentence;
		private final String whWord;
		private final String whWordPos;
		private final String posOfNext;
		private final String rootPos;

		public Sentence getSentence() {
			return sentence;
		}

		public String getWhWord() {
			return whWord;
		}

		public String getWhWordPos() {
			return whWordPos;
		}

		public String getPosOfNext() {
			return posOfNext;
		}

		public String getRootPos() {
			return rootPos;
		}

		public Features(Sentence sentence, String whWord, String whWordPos, String posOfNext, String rootPos) {
			this.sentence = sentence;
			this.whWord = whWord;
			this.whWordPos = whWordPos;
			this.posOfNext = posOfNext;
			this.rootPos = rootPos;
		}

		@Override
		public String toString() {
			return whWord + "," +
				whWordPos + "," +
				posOfNext + "," +
				rootPos;
		}
	}

	public QuestionTypeClassifier.Features classifyFeatures(Sentence sentence) {
		final List<Token> tokens = sentence.tokens();

		String rootPos = tokens
			.stream()
			.filter(Token::hasRootAsDependency)
			.map(Token::partOfSpeech)
			.findFirst()
			.get();

		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (POS.isWhWord(token)) {
				String wh = token.lemma();
				String pos = token.partOfSpeech();
				Token nextToken = tokens.get(i + 1);
				String posOfNext = nextToken.partOfSpeech();
				return new Features(sentence, wh, pos, posOfNext, rootPos);
			}
		}
		throw new RuntimeException("Can't classify");
	}

}
