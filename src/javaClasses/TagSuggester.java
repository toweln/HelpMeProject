package edu.brown.cs.acj.helpme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * a class to suggest tags of a question based on its message.
 * 
 * @author andrewjones
 *
 */
public class TagSuggester {

	private Question q;
	private TagDatabase td;

	/**
	 * create new TagSuggester.
	 * 
	 * @param td
	 *            the database of tags.
	 */
	public TagSuggester(TagDatabase td) {
		this.td = td;
	}

	/**
	 * get a map of suggested tags to their compatibilities.
	 * 
	 * @param q
	 *            the question.
	 * @return tags and associated compatibilities.
	 */
	public Map<Tag, Double> getTagSuggestions(Question q) {
		Map<Tag, Double> tagCompatibilities = new HashMap<>();
		Map<String, Integer> questionCount = q.getWordCount()
				.getUnigramCounts();
		for (Discipline d : td.getTaxonomy()) {
			for (Tag t : d.getSubdisciplines()) {
				Map<String, Integer> tagCount = t.getWordCount()
						.getUnigramCounts();
				double dotProd = 0;
				double tagLength = 0;// t.getWordCount().getNumWords();
				for (String word : tagCount.keySet()) {
					tagLength += Math.pow(tagCount.get(word), 2);
				}
				double questionLength = 0;// q.getWordCount().getNumWords();
				for (String word : questionCount.keySet()) {
					questionLength += Math.pow(questionCount.get(word), 2);
					if (tagCount.keySet().contains(word)) {
						// tagLength += Math.pow(tagCount.get(word), 2);
						dotProd += questionCount.get(word) * tagCount.get(word);
					} else {
						continue;
					}
				}
				double vectorDist = Math.pow(tagLength, 0.5)
						* Math.pow(questionLength, 0.5);
				double tagCompat = Math.round((dotProd / vectorDist) * 1000.0)
						/ 1000.0;
				tagCompatibilities.put(t, tagCompat);
				if ((dotProd / vectorDist) > 0) {
//					System.out.println(
//							t.getName() + ": " + (dotProd / vectorDist));
					// System.out.println(" DOT PROD: " + dotProd);
					// System.out.println(" TAG LENGTH: " + tagLength);
					// System.out.println(" QUESTION LENGTH: " +
					// questionLength);
					// System.out.println(" DIST: " + vectorDist);
				}
			}
		}
		return tagCompatibilities;
	}

	/**
	 * sort the map of tags and compatibilities.
	 * 
	 * @param unsortMap
	 *            the unsorted map.
	 * @return the sorted map.
	 */
	private static Map<Tag, Double> sortByComparator(
			Map<Tag, Double> unsortMap) {

		// Convert Map to List
		List<Map.Entry<Tag, Double>> list = new LinkedList<Map.Entry<Tag, Double>>(
				unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Tag, Double>>() {
			public int compare(Map.Entry<Tag, Double> o1,
					Map.Entry<Tag, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// Convert sorted map back to a Map
		Map<Tag, Double> sortedMap = new LinkedHashMap<Tag, Double>();
		for (Iterator<Map.Entry<Tag, Double>> it = list.iterator(); it
				.hasNext();) {
			Map.Entry<Tag, Double> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/**
	 * get a list of strings representing most highly suggested tags.
	 * 
	 * @param q
	 *            the question.
	 * @return the list of suggested strings (tags).
	 */
	public List<String> getTopTagSuggestions(Question q) {
		Map<Tag, Double> tagCompats = getTagSuggestions(q);
		Map<Tag, Double> sortedTags = sortByComparator(tagCompats);
		List<String> topTagNames = new ArrayList<>();
		for (Entry<Tag, Double> entry : sortedTags.entrySet()) {
			if (entry.getValue() > 0) {
				topTagNames.add(entry.getKey().getName());
			}
		}
		return topTagNames;
	}

	/**
	 * get the question.
	 * 
	 * @return the question
	 */
	public Question getQuestion() {
		return q;
	}

}
