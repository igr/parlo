package com.oblac.parlo.processing;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeDependencyUtil {

	public static void main(String[] args) {
		System.out.println(TypeDependencyUtil.getData("Jodd loves Java."));
	}

	private static final LexicalizedParser parser = LexicalizedParser.loadModel();
	private static final PennTreebankLanguagePack languagePack = new PennTreebankLanguagePack();
	private static final GrammaticalStructureFactory structureFactory = languagePack.grammaticalStructureFactory();

	private static final Map<String, List<TypedDependency>> map = new HashMap<>();

	public static List<TypedDependency> getTypeDependencies(final String text) {
		if (map.isEmpty()) {
			System.err.println("Loading type dependencies....");
			try {
				final ObjectInputStream writer = new ObjectInputStream(new FileInputStream("data/typed-dependencies.bin"));
				map.putAll((Map<String, List<TypedDependency>>) writer.readObject());
			} catch (final Exception e) {
				System.out.println("Failed to load typed dependencies");
			}
			System.err.println("Loaded type dependencies");
		}
		return map.get(text);
//
	}

	public static TypeDependencyData getData(final String text) {
		List<TypedDependency> list = getTypeDependencies(text);
		if (list == null) {
//            System.err.println("Missing dependencies for: " + text);
			list = structureFactory.newGrammaticalStructure(parser.parse(text)).typedDependenciesCCprocessed();
		}
		return new TypeDependencyData(getSubject(list), getRelation(list), getObject(list));
	}

	private static String getSubject(final List<TypedDependency> dependencies) {
		String rootSubject = null, subject = null;
		for (int i = dependencies.size() - 1; i >= 0; i--) {
			final TypedDependency dependency = dependencies.get(i);
			if (dependency.reln().toString().contains("subj")) {
				rootSubject = subject = dependency.dep().word();
			} else if (dependency.reln().toString().contains("compound") && dependency.gov().word().equals(rootSubject)) {
				subject = dependency.dep().word() + " " + subject;
			}
		}
		if (subject == null) {
			return null;
		}
		String lemmaSubject = "";
		for (final String lemma : new Sentence(subject).lemmas()) {
			lemmaSubject += lemma + " ";
		}
		return lemmaSubject.trim();
	}

	private static String getRelation(final List<TypedDependency> dependencies) {
		String relation = null;
		for (final TypedDependency dependency : dependencies) {
			if (dependency.reln().toString().contains("root")) {
				relation = dependency.dep().word();
			}
		}
//            else if (dependency.reln().toString().contains("cop") && dependency.gov().word().equals(relation))
//                relation = dependency.dep().word();
		return relation == null ? null : new Sentence(relation).lemmas().get(0);
	}

	private static String getObject(final List<TypedDependency> dependencies) {
		String rootDirectObject = null, directObject = null;
		for (int i = dependencies.size() - 1; i >= 0; i--) {
			final TypedDependency dependency = dependencies.get(i);
			if (dependency.reln().toString().contains("dobj")) {
				rootDirectObject = directObject = dependency.dep().word();
			} else if (dependency.reln().toString().contains("compound") && dependency.gov().word().equals(rootDirectObject)) {
				directObject = dependency.dep().word() + " " + directObject;
			}
		}
		if (directObject == null) {
			return null;
		}
		String lemmaDirectObject = "";
		for (final String lemma : new Sentence(directObject).lemmas()) {
			lemmaDirectObject += lemma + " ";
		}
		return lemmaDirectObject.trim();
	}

	// TODO: merge with triples
	public static Map<String, String> getAbbreviations(final String text) {
		final List<TypedDependency> dependencies = structureFactory.newGrammaticalStructure(parser.parse(text))
			.typedDependenciesCCprocessed();

		final Map<String, String> abbreviations = new HashMap<>();

		String key = null, rootValue = null, value = null;
		for (int i = dependencies.size() - 1; i >= 0; i--) {
			final TypedDependency dependency = dependencies.get(i);
			if (dependency.reln().toString().contains("appos")) {
				if (key != null) {
					abbreviations.put(key, value);
				}
				key = dependency.dep().word();
				value = rootValue = dependency.gov().word();
			} else if (key != null && dependency.reln().toString().contains("compound") && dependency.gov().word().contains(rootValue)) {
				value = dependency.dep().word() + " " + value;
			}
		}
		if (key != null) {
			abbreviations.put(key, value);
		}
		return abbreviations;
	}

	public static class TypeDependencyData {

		private final String subject;
		private final String relation;
		private final String object;

		public TypeDependencyData(String subject, String relation, String object) {
			this.subject = subject;
			this.relation = relation;
			this.object = object;
		}

		public String getSubject() {
			return subject;
		}

		public String getRelation() {
			return relation;
		}

		public String getObject() {
			return object;
		}

		@Override
		public String toString() {
			return "TypeDependencyData{" +
				"subject='" + subject + '\'' +
				", relation='" + relation + '\'' +
				", object='" + object + '\'' +
				'}';
		}
	}
}
