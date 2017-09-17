package com.oblac.parlo.classifier.question;

import com.oblac.parlo.QuestionType;
import com.oblac.parlo.Sentence;
import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils;

public class QuestionClassifier {

	public static final String FOLDER = "data/classifier-question/";
	protected Classifier classifier;
	protected Instances dataSet;
	protected QuestionTypeClassifier questionTypeClassifier = new QuestionTypeClassifier();

	public void train() {
		try {
			Classifier randomForest = new RandomForest();

			ConverterUtils.DataSource source = new ConverterUtils.DataSource(FOLDER + "question-classifier.arff");
			dataSet = source.getDataSet();

			dataSet.setClassIndex(dataSet.numAttributes() - 1);
			randomForest.buildClassifier(dataSet);

			classifier = randomForest;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public QuestionType classifyQuestion(Sentence sentence) {
		if (!sentence.isQuestion()) {
			return QuestionType.NA;
		}

		QuestionTypeClassifier.Features features = questionTypeClassifier.classifyFeatures(sentence);

		Instance inst = new DenseInstance(5);
		inst.setDataset(dataSet);

		inst.setValue(0, features.getWhWord());
		inst.setValue(1, features.getWhWordPos());
		inst.setValue(2, features.getPosOfNext());
		inst.setValue(3, features.getRootPos());
		inst.setValue(4, Utils.missingValue());

		try {
			int ndx = (int) classifier.classifyInstance(inst);
			 return QuestionType.valueOf(ndx);
		} catch (Exception e) {
			throw new RuntimeException("Not classified");
		}
	}
}
