package edu.brown.cs.HelpMe.autocorrect;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.brown.cs.HelpMe.Trie.Trie;
import edu.brown.cs.HelpMe.Trie.TrieNode;
import edu.brown.cs.HelpMe.main.Discipline;
import edu.brown.cs.HelpMe.main.Tag;
import edu.brown.cs.HelpMe.main.TagDatabase;

/**
 * Suggests all possible words from a trie based on specified suggestion
 * parameters.
 *
 * @author acj
 *
 */
public class SuggestionGenerator {

	private CommandParser cp;
	private List<String> allTagNames;
	private Trie trie;
	private static final int NUM_SUGGESTIONS = 5;

	/**
	 * Build new generator.
	 *
	 * @param cp
	 *            command parser with all suggestion specs.
	 * @throws FileNotFoundException
	 *             if file does not exist.
	 * @throws SQLException
	 *             if the database doesn't exist.
	 * @throws ClassNotFoundException
	 *             if the SQL connection is invalid.
	 */
	public SuggestionGenerator(TagDatabase td, CommandParser cp)
			throws FileNotFoundException, SQLException, ClassNotFoundException {
		this.cp = cp;
		allTagNames = new ArrayList<>();
		for (Discipline d : td.getTaxonomy()) {
			for (Tag t : d.getSubdisciplines()) {
				allTagNames.add(t.getName().toLowerCase());
			}
		}
		// System.out.println(allActorNames);
		if (allTagNames.size() == 0) {
			throw new IllegalStateException();
		}
		Trie tempTrie = new Trie();
		for (String t : allTagNames) {
			tempTrie.addWord(t);
		}
		this.trie = tempTrie;

	}

	/**
	 * get list of suggested fills.
	 *
	 * @param userInput
	 *            input from user.
	 * @return list of possible fills.
	 */
	public List<String> getSuggestions(String userInput) {
		String strippedInput = userInput.toLowerCase().replaceAll("[^a-zA-Z ]",
				" ");
		if (strippedInput.substring(0, 1).equals(" ")) {
			strippedInput = strippedInput.substring(1);
		}

		List<String> suggestions = new ArrayList<>(
				filterSuggestions(strippedInput, trie));
		return suggestions;
	}

	private List<String> filterSuggestions(String strippedInput,
			Trie wordTrie) {

		Set<String> outputString = new HashSet<>();

		// String lastWord = wordList[wordList.length - 1];
		String lastWord = strippedInput;

		if (cp.getled()) {
			outputString.addAll(getLEDs(lastWord, cp.getledmaxdist(), trie));
		}
		if (cp.getPrefix()) {
			outputString.addAll(getPrefixFills(lastWord, trie));
		}
		if (cp.getWhitespace()) {
			outputString.addAll(whitespace(lastWord, trie));
		}

		List<String> sortOutput = new ArrayList<>(outputString);

		List<String> fullSent = new ArrayList<>();

		int maxInd = NUM_SUGGESTIONS;
		if (wordTrie.containsWord(lastWord)) {
			if (sortOutput.contains(lastWord)) {
				sortOutput.remove(sortOutput.indexOf(lastWord));
			}
			fullSent.add(0, lastWord);
		}
		if (sortOutput.size() < NUM_SUGGESTIONS) {
			maxInd = sortOutput.size();
		}
		if (sortOutput.size() == 0) {
			return fullSent;
		} else {
			for (int j = 0; j < maxInd; j++) {
				fullSent.add(sortOutput.get(j));
			}
		}
		// System.out.println(sortOutput);
		return fullSent;
	}

	/**
	 * Function for prefix matching.
	 *
	 * @param prefix
	 *            input string.
	 * @param wordTrie
	 *            trie full of words.
	 * @return set of all prefix suggestions.
	 */
	public Set<String> getPrefixFills(String prefix, Trie wordTrie) {
		Set<String> fullWords = new HashSet<>();
		TrieNode startNode = wordTrie.getLastLetterNode(prefix);
		getPrefixFillsHelper(startNode, prefix, fullWords);
		return fullWords;
	}

	private void getPrefixFillsHelper(TrieNode currNode, String prefix,
			Set<String> fullWords) {
		if (currNode == null) {
			return;
		}
		if (currNode.getIsWord()) {
			fullWords.add(prefix);
		}
		for (int i = 0; i < currNode.getChildren().length; i++) {
			if (currNode.getChildren()[i] != null) {
				getPrefixFillsHelper(currNode.getChildren()[i],
						prefix + currNode.getChildren()[i].getLetter(),
						fullWords);
			}
		}
	}

	/**
	 * Makes an array of numbers 0 to max + 1, inclusive, in ascending order.
	 * Helps to create the table to calculate LEDs.
	 *
	 * @param max
	 *            max value of the array
	 * @return list of integers in ascending order
	 */
	int[] arrayRange(int max) {
		int i = 0;
		int[] range = new int[max + 1];
		while (i <= max) {
			range[i] = i;
			i += 1;
		}
		return range;
	}

	private static int getMin(int[] arr) {
		int currMin = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] < currMin) {
				currMin = arr[i];
			}
		}
		return currMin;
	}

	/**
	 * Finds suggestions by insertions, deletions, and substitutions based on a
	 * maximum LED value.
	 *
	 * @param word
	 *            word to find suggestions based off of
	 * @param maxDist
	 *            maximum Lev. edit distance
	 * @param wordTrie
	 *            trie that is holding our data
	 * @return set of strings based on LED suggestions
	 */
	public Set<String> getLEDs(String word, int maxDist, Trie wordTrie) {
		Set<String> changedWords = new HashSet<>();

		int[] currRow = arrayRange(word.length());

		for (int i = 0; i < wordTrie.getRoot().getChildren().length; i++) {
			TrieNode topNode = wordTrie.getRoot().getChildren()[i];
			if (topNode != null) {
				getLEDsHelper(word, topNode, currRow, topNode.getLetter(),
						changedWords, maxDist, wordTrie);
			}
		}
		return changedWords;
	}

	private void getLEDsHelper(String word, TrieNode topNode, int[] prevRow,
			char startLetter, Set<String> changedWords, int maxDist,
			Trie wordTrie) {

		int[] currRow = new int[prevRow.length];
		currRow[0] = prevRow[0] + 1;

		int numColumns = word.length() + 1;

		for (int i = 1; i < numColumns; i++) {
			int deleteDist = prevRow[i] + 1;
			int insertDist = currRow[i - 1] + 1;

			int subDist;
			if (word.charAt(i - 1) == startLetter) {
				subDist = prevRow[i - 1];
			} else {
				subDist = prevRow[i - 1] + 1;
			}

			int minDist = Math.min(Math.min(deleteDist, insertDist), subDist);
			currRow[i] = minDist;
		}

		if (currRow[currRow.length - 1] <= maxDist && topNode.getIsWord()) {
			changedWords.add(wordTrie.stringFromNode("", topNode));
		}

		if (getMin(currRow) <= maxDist) {
			for (int j = 0; j < wordTrie.getRoot().getChildren().length; j++) {
				if (topNode.getChildren()[j] != null) {
					getLEDsHelper(word, topNode.getChildren()[j], currRow,
							topNode.getChildren()[j].getLetter(), changedWords,
							maxDist, wordTrie);
				}
			}
		}
	}

	/**
	 * Finds suggestions based on splitting whitespace.
	 *
	 * @param word
	 *            word that could potentially be split
	 * @param wordTrie
	 *            trie in which we are holding our data
	 * @return a set of strings that are valid whitespace splits
	 */
	public Set<String> whitespace(String word, Trie wordTrie) {
		Set<String> validSplits = new HashSet<>();

		for (int i = 1; i < word.length() - 1; i++) {
			String substring1 = word.substring(0, i);
			String substring2 = word.substring(i);
			if (wordTrie.containsWord(substring1)
					&& wordTrie.containsWord(substring2)) {
				validSplits.add(substring1 + " " + substring2);
			}
		}
		return validSplits;
	}

	/**
	 * get command parser.
	 *
	 * @return command parser.
	 */
	public CommandParser getCP() {
		return cp;
	}

	/**
	 * get trie.
	 *
	 * @return trie.
	 */
	public Trie getTrie() {
		return trie;
	}
}
