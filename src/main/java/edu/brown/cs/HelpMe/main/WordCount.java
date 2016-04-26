package edu.brown.cs.HelpMe.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * a class to keep track of word counts for each user and question.
 * @author andrewjones
 *
 */
public class WordCount {

	private Map<String, Integer> unigramCounts;
	private Set<String> dictionary;
	private int numWords;

	/**
	 * initialize a WordCount of a message associated with a tutoring account.
	 * 
	 * @param message
	 *            the message of the request.
	 */
	public WordCount(String message) {

		// initialize a WordCount by finding unigram counts.
		message = message.toLowerCase().replaceAll("[^a-zA-Z ]", " ");
		String[] words = message.split("\\s+");
		this.dictionary = new HashSet<>();
		this.unigramCounts = new HashMap<>();
		this.numWords = 0;

		for (String s : words) {
			s = s.replaceAll("\\s+", "");
			dictionary.add(s);
			if (s.equals("")) {
				continue;
			}
			numWords += 1;
			if (unigramCounts.containsKey(s)) {
				int uniCount = unigramCounts.get(s);
				unigramCounts.put(s, uniCount + 1);
			} else {
				unigramCounts.put(s, 1);
			}
		}
	}

	/**
	 * initialize an empty wordcount.
	 */
	public WordCount() {
		this.dictionary = new HashSet<>();
		this.unigramCounts = new HashMap<>();
		this.numWords = 0;
	}

	/**
	 * update a wordcount with a new message.
	 * @param message the message.
	 */
	public void updateWordCount(String message) {
		message = message.toLowerCase().replaceAll("[^a-zA-Z ]", " ");
		String[] words = message.split("\\s+");

		for (String s : words) {
			s = s.replaceAll("\\s+", "");
			dictionary.add(s);
			if (s.equals("")) {
				continue;
			}
			numWords += 1;
			if (unigramCounts.containsKey(s)) {
				int uniCount = unigramCounts.get(s);
				unigramCounts.put(s, uniCount + 1);
			} else {
				unigramCounts.put(s, 1);
			}
		}
	}

	/**
	 * get the unigram counts of the count.
	 * @return a map of word to count.
	 */
	public Map<String, Integer> getUnigramCounts() {
		return unigramCounts;
	}

	/**
	 * total number of words.
	 * @return number of words.
	 */
	public int getNumWords() {
		return numWords;
	}

	/**
	 * increment total word count by 1.
	 */
	public void addNumWord() {
		numWords += 1;
	}

}
