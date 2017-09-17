package com.oblac.parlo;

import com.oblac.parlo.answer.AnswerFinder;
import jodd.io.FileUtil;

import java.io.IOException;
import java.util.Scanner;

public class Parlo {

	public static void main(String[] args) throws IOException {
		// read some text from the file
		String storyText = FileUtil.readString("data/story.txt");

		StoryParser storyParser = new StoryParser();

		Story story = storyParser.parseText(storyText);

		// run me
		AnswerFinder answerFinder = new AnswerFinder(storyParser);

		Scanner reader = new Scanner(System.in);
		while (true) {
			System.out.println(">> ");
			String input = reader.nextLine().trim().toLowerCase();

			if (input.equals("exit")) {
				System.out.println("Bye!");
				break;
			}

			answerFinder.answer(story, input);
		}
	}
}