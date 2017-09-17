package com.oblac.parlo;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

/**
 * Factory for Stanford CoreNLP library.
 */
public class CoreNLP {

	public static final String DATA_DIR = "edu/stanford/nlp/models/";
	public static final String RESOURCES_DIR = "data/";

	/**
	 * Creates and configures new instance.
	 */
	public static StanfordCoreNLP createStanfordCoreNLP() {
		// creates a StanfordCoreNLP object
		Properties props = new Properties();

		// tokenize - Tokenizes the text into a sequence of tokens, roughly the "words".
		// ssplit - Splits a sequence of tokens into sentences.
		// pos - s Labels tokens with their part-of-speech (POS) tags.
		// lemma - Generates the lemmas (base forms) for all tokens in the annotation.
		// ner - Recognizes named (PERSON, LOCATION, ORGANIZATION, MISC) and numerical (MONEY, NUMBER, DATE, TIME, DURATION, SET) entities.
		// parse - Provides full syntactic analysis, including both constituent and dependency representation, based on a probabilistic parser.
		// sentiment - Sentiment analysis with a compositional model over trees using deep learning
		// natlog - Marks quantifier scope and token polarity, according to natural logic semantics.
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, sentiment, natlog, openie, regexner, regexner1, regexner2, dcoref");

		//props.setProperty("pos.model", DATA_DIR + "pos-tagger/english-left3words/english-left3words-distsim.tagger");
		props.setProperty("pos.model", DATA_DIR + "pos-tagger/english-bidirectional/english-bidirectional-distsim.tagger");

		props.setProperty("parse.model", DATA_DIR + "lexparser/englishPCFG.ser.gz");
		props.setProperty("parse.binaryTrees", "true");
		props.setProperty("ner.model",
				DATA_DIR + "ner/english.all.3class.caseless.distsim.crf.ser.gz,"
				+ DATA_DIR + "ner/english.conll.4class.caseless.distsim.crf.ser.gz,"
				+ DATA_DIR + "ner/english.muc.7class.caseless.distsim.crf.ser.gz");
		props.setProperty("parse.extradependencies", "ref_only_uncollapsed");

		props.setProperty("sutime.rules",
			DATA_DIR + "sutime/defs.sutime.txt,"
			+ DATA_DIR + "sutime/english.sutime.txt,"
			+ DATA_DIR + "sutime/english.holidays.sutime.txt");

		// uses Universal Dependencies (http://universaldependencies.org/docs/)
		props.setProperty("depparse.model", DATA_DIR + "parser/nndep/english_UD.gz");
		props.setProperty("depparse.extradependencies","ref_only_uncollapsed");

		props.setProperty("openie.splitter.model", DATA_DIR + "naturalli/clauseSearcherModel.ser.gz");
		props.setProperty("openie.affinity_models", DATA_DIR + "naturalli/affinities");
		props.setProperty("openie.splitter.threshold", "0.10");
		props.setProperty("openie.optimze_for", "GENERAL");
		props.setProperty("openie.ignoreaffinity", "false");
		props.setProperty("openie.max_entailments_per_clause", "1000");
		props.setProperty("openie.triple.strict", "true");

		props.put("regexner.mapping", RESOURCES_DIR + "ner/domain.tab");

		props.put("customAnnotatorClass.regexner1", "edu.stanford.nlp.pipeline.TokensRegexNERAnnotator");
		props.put("regexner1.mapping", RESOURCES_DIR + "ner/kbp_regexner_mapping_nocase.tab");
		props.put("regexner1.validpospattern"," ^(NN|JJ).*");
		props.put("regexner1.ignorecase", "true");
		props.put("regexner1.noDefaultOverwriteLabels", "CITY");

		props.put("customAnnotatorClass.regexner2", "edu.stanford.nlp.pipeline.TokensRegexNERAnnotator");
		props.put("regexner2.mapping", RESOURCES_DIR + "ner//kbp_regexner_mapping.tab");
		props.put("regexner2.ignorecase", "false");
		props.put("regexner2.noDefaultOverwriteLabels", "CITY");

		return new StanfordCoreNLP(props);
	}

}
