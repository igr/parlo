package com.oblac.parlo.classifier.sentence;

import com.oblac.parlo.Sentence;
import com.oblac.parlo.StoryParser;
import jodd.io.FileUtil;
import jodd.json.JsonParser;
import jodd.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArffGenerator {

	public static final String FOLDER = "etc/classifier-sentence/";

	public static void main(String[] args) throws IOException {
		List<String> arffLines = new ArrayList<>();

		StoryParser storyParser = new StoryParser(false);

		String[] txt = FileUtil.readLines(FOLDER + "expressives.txt");
		arffLines.addAll(processExpressives(storyParser, txt));

		String json = FileUtil.readString(FOLDER + "train-v1.1.json");
		arffLines.addAll(processQAs(storyParser, json));

		arffLines =
			arffLines.stream()
				.parallel()
				.filter(line -> !StringUtil.isBlank(line))
				.collect(Collectors.toList());

		System.out.println("Done");
		System.out.println("Total :" + arffLines.size());

		int line = 0;
		arffLines.add(line++, "@relation speechacts");
		arffLines.add(line++, "");
		arffLines.add(line++, "@attribute       sen_len            numeric");
		arffLines.add(line++, "@attribute       nn_num             numeric");
		arffLines.add(line++, "@attribute       end_in_n           numeric");
		arffLines.add(line++, "@attribute       begin_v            numeric");
		arffLines.add(line++, "@attribute       wh_num             numeric");
		arffLines.add(line++, "@attribute       type               {assertion,question,expressive}");
		arffLines.add(line++, "");
		arffLines.add(line++, "@data");

		arffLines.stream()
			.reduce((first, second) -> first + "\n" + second)
			.ifPresent(out -> {
				try {
					FileUtil.writeString(FOLDER + "speech-act-classifier.arff", out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
	}

	private static List<String> processExpressives(StoryParser storyParser, String[] txt) {
		List<String> arffLines = new ArrayList<>();

		for (String line : txt) {
			storyParser
				.parseText(line)
				.sentences()
				.forEach(sentence ->
					arffLines.add(classifySentence(sentence, "expressive")));
		}
		return arffLines;
	}

	private static List<String> processQAs(StoryParser storyParser, String json) {
		Map<String, Object> jsonMap = JsonParser.create().parse(json);

		List<String> arffLines = new ArrayList<>();

		List<Map<String, Object>> data = (List) jsonMap.get("data");

		for (Map<String, Object> datum : data) {
			List<Map<String, Object>> paragraphs = (List) datum.get("paragraphs");

			for (Map<String, Object> paragraph : paragraphs) {
				List<Map<String, Object>> qas = (List) paragraph.get("qas");
				for (Map<String, Object> qa : qas) {
					String question = (String) qa.get("question");
					storyParser
						.parseText(question)
						.sentences()
						.forEach(sentence ->
							arffLines.add(classifySentence(sentence, "question")));
				}

				String context = (String) paragraph.get("context");
				int ndx = context.indexOf(".");

				if (ndx >= 0) {
					String assertion = context.substring(0, ndx);
					storyParser
						.parseText(assertion)
						.sentences()
						.forEach(sentence ->
							arffLines.add(classifySentence(sentence, "assertion")));
				}
			}
		}

		return arffLines;
	}

	private static String classifySentence(Sentence sentence, String type) {
		SpeechActsClassifier speechActsClassifier = new SpeechActsClassifier();
		try {
			SpeechActsClassifier.Features features = speechActsClassifier.classifyFeatures(sentence);
			return features.toString() + "," + type;
		} catch (Exception ignore) {
			return "";
		}
	}
}
