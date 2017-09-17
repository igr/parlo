package com.oblac.parlo.classifier.sentence;

import com.oblac.parlo.Sentence;
import com.oblac.parlo.SentenceType;
import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils;

public class SentenceClassifier {
	public static final String FOLDER = "data/classifier-sentence/";
	protected Classifier classifier;
	protected Instances dataSet;
	protected SpeechActsClassifier speechActsClassifier = new SpeechActsClassifier();

	public void trainBig() {
		train("speech-acts-classifier.arff");
	}
	public void trainSmall() {
		train("speech-acts-small.arff");
	}
	private void train(String name) {
		try {
			Classifier randomForest = new RandomForest();

			ConverterUtils.DataSource source = new ConverterUtils.DataSource(FOLDER + name);
			dataSet = source.getDataSet();

			dataSet.setClassIndex(dataSet.numAttributes() - 1);
			randomForest.buildClassifier(dataSet);

			classifier = randomForest;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SentenceType classifySentence(Sentence sentence) {
		SpeechActsClassifier.Features features = speechActsClassifier.classifyFeatures(sentence);

		Instance inst = new DenseInstance(6);
		inst.setDataset(dataSet);

		inst.setValue(0, features.getSentenceLength());
		inst.setValue(1, features.getNumberOfNouns());
		inst.setValue(2, (features.isEndingInNounOrAdjective() ? 1 : 0));
		inst.setValue(3, (features.isBeginningInVerb() ? 1 : 0));
		inst.setValue(4, features.getCountOfWhMarkers());
		inst.setValue(5, Utils.missingValue());

		try {
			return SentenceType.valueOf(classifier.classifyInstance(inst));
		} catch (Exception e) {
			throw new RuntimeException("Can't classify");
		}
	}

}
