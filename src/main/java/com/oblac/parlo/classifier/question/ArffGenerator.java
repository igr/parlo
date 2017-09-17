package com.oblac.parlo.classifier.question;

import com.oblac.parlo.POS;
import com.oblac.parlo.Sentence;
import com.oblac.parlo.StoryParser;
import jodd.io.FileUtil;
import jodd.util.StringUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ArffGenerator {

	public static final String FOLDER = "etc/classifier-question/";
	public static final String[] ALL_FULL_TYPES = {
		"ABBR:abb",
		"ABBR:exp",
		"DESC:def",
		"DESC:desc",
		"DESC:manner",
		"DESC:reason",
		"ENTY:animal",
		"ENTY:body",
		"ENTY:color",
		"ENTY:cremat",
		"ENTY:currency",
		"ENTY:dismed",
		"ENTY:event",
		"ENTY:food",
		"ENTY:instru",
		"ENTY:lang",
		"ENTY:letter",
		"ENTY:other",
		"ENTY:plant",
		"ENTY:product",
		"ENTY:religion",
		"ENTY:sport",
		"ENTY:substance",
		"ENTY:symbol",
		"ENTY:techmeth",
		"ENTY:termeq",
		"ENTY:veh",
		"ENTY:word",
		"HUM:desc",
		"HUM:gr",
		"HUM:ind",
		"HUM:title",
		"LOC:city",
		"LOC:country",
		"LOC:mount",
		"LOC:other",
		"LOC:state",
		"NUM:code",
		"NUM:count",
		"NUM:date",
		"NUM:dist",
		"NUM:money",
		"NUM:ord",
		"NUM:other",
		"NUM:perc",
		"NUM:period",
		"NUM:speed",
		"NUM:temp",
		"NUM:volsize",
		"NUM:weight",
	};

	public static final String[] ALL_TYPES = {
		"ABBR",
		"DESC",
		"ENTY",
		"HUM",
		"LOC",
		"NUM",
	};



	public static final Set<String> TYPES = new HashSet<>(Arrays.asList(ALL_TYPES));

	public static void main(String[] args) throws IOException {
		List<String> arffLines = new ArrayList<>();

		StoryParser storyParser = new StoryParser(false);

		String[] txt = FileUtil.readLines(FOLDER + "train_5500.label.txt");
		arffLines.addAll(processInput(storyParser, txt));

		arffLines =
			arffLines.stream()
				.parallel()
				.filter(line -> !StringUtil.isBlank(line))
				.collect(Collectors.toList());

		System.out.println("Done");
		System.out.println("Total :" + arffLines.size());

		int line = 0;
		arffLines.add(line++, "@relation qtype");
		arffLines.add(line++, "");
		arffLines.add(line++, "@attribute       wh_word            {how,what,where,who,which,when,why,whose,that,whom}");
		arffLines.add(line++, "@attribute       wh_word_pos        {WRB,WDT,WP,WP$}");
		arffLines.add(line++, "@attribute       pos_of_next        {" + POS.buildPosString() + "}");
		arffLines.add(line++, "@attribute       root_pos           {" + POS.buildPosString() + "}");
		arffLines.add(line++, "@attribute       type               {" + StringUtil.join(ALL_TYPES, ',') + "}");
		arffLines.add(line++, "");
		arffLines.add(line++, "@data");

		arffLines.stream()
			.reduce((first, second) -> first + "\n" + second)
			.ifPresent(out -> {
				try {
					FileUtil.writeString(FOLDER + "question-classifier.arff", out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
	}

	private static List<String> processInput(StoryParser storyParser, String[] txt) {
		List<String> arffLines = new ArrayList<>();

		int counter = 0;

		for (String line : txt) {
			System.out.println(counter++);
			int ndx = line.indexOf(' ');
			String type = line.substring(0, ndx);
			line = line.substring(ndx + 1);

			storyParser
				.parseText(line)
				.sentences()
				.forEach(sentence ->
					arffLines.add(classifySentence(sentence, type)));
		}
		return arffLines;
	}

	private static String classifySentence(Sentence sentence, String type) {
		type = StringUtil.cutToIndexOf(type, ':');

		if (!TYPES.contains(type)) {
			throw new RuntimeException("Unknown type: " + type);
		}

		QuestionTypeClassifier questionTypeClassifier = new QuestionTypeClassifier();

		try {
			QuestionTypeClassifier.Features features = questionTypeClassifier.classifyFeatures(sentence);

			if (features.getPosOfNext().equals("``")) {
				return "";
			}

			return features.toString() + "," + type;
		}
		catch (Exception ignore) {
			return "";
		}
	}

}
