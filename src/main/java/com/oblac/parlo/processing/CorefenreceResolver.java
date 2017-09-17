package com.oblac.parlo.processing;

import com.oblac.parlo.Story;
import com.oblac.parlo.Token;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.util.Pair;

import java.util.*;

/**
 * Coreference resolver.
 */
public class CorefenreceResolver {

	public String[] coreferenceResolution(Story story, String question) {
		String content = story.text();
		Map<Integer, CorefChain> graph = story.corefChainMap();

		Map<String, String> questionReplace = new HashMap<>();
		//Map<Pair<Integer, Integer>, String> answer = new HashMap<>();
		Map<Pair<Integer, Integer>, String> replaceList = new HashMap<>();

		for (Map.Entry<Integer, CorefChain> entry : graph.entrySet()) {
			CorefChain c = entry.getValue();
			if (c.getMentionsInTextualOrder().size() <= 1) {
				continue;
			}
			CorefChain.CorefMention cm = c.getRepresentativeMention();
			String clust = "";
			List<Token> tks = story.sentences().get(cm.sentNum - 1).tokens();
			int r = 0;
			for (int i = cm.startIndex - 1; i < cm.endIndex - 1; i++) {
				if (!tks.get(i).partOfSpeech().matches("PRP|PRP\\$")) {
					r = 1;
				}
				clust += tks.get(i).word() + " ";
			}
			clust = clust.trim();
			for (CorefChain.CorefMention m : c.getMentionsInTextualOrder()) {
				String token = story.sentences().get(m.sentNum - 1).tokens().get(m.startIndex - 1).pos();
				int index = story.sentences().get(m.sentNum - 1).tokens().get(m.startIndex - 1).characterOffsetBegin();
				int index1 = story.sentences().get(m.sentNum - 1).tokens().get(m.startIndex - 1).characterOffsetEnd();
				questionReplace.put(token, clust);

				if (token.matches("PRP|PRP\\$") && r == 1) {
					replaceList.put(new Pair<>(index, index1), clust);
					//answer.put(new Pair<>(m.sentNum - 1, m.startIndex - 1), clust);
				}
			}
		}

		SortedSet<Pair<Integer, Integer>> keys = new TreeSet<>(replaceList.keySet());
		List<Pair<Integer, Integer>> keySet = new ArrayList<>();
		keySet.addAll(keys);
		Collections.reverse(keySet);

		for (Pair<Integer, Integer> key : keySet) {
			content = content.substring(0, key.first) + replaceList.get(key) + content.substring(key.second);
		}

		return new String[] {content, question};
	}

}
