package edu.brown.cs.HelpMe.Trie;

/**
 * Trie data structure, usually used to store strings.
 * 
 * @author acj
 */
public class Trie {

	/**
	 * The root (top node) of the trie.
	 */
	private TrieNode root;

	/**
	 * Constructor for a trie.
	 */
	public Trie() {
		this.root = new TrieNode('\0');
	}

	/**
	 * Returns root of the tree.
	 * 
	 * @return TrieNode representing the root of the trie.
	 */
	public TrieNode getRoot() {
		return root;
	}

	/**
	 * Adds a word to the Trie.
	 * 
	 * @param word
	 *            word (string) to be added to the trie
	 */
	public void addWord(String word) {
		addWordHelper(root, word);
	}

	/**
	 * Helper function for adding words to the trie.
	 * 
	 * @param currNode
	 *            Current node that the function is visiting.
	 * @param word
	 *            Word that is being added.
	 */
	private void addWordHelper(TrieNode currNode, String word) {
		if (word.length() == 0) {
			return;
		}
		char firstLetter = word.charAt(0);
		// System.out.println(firstLetter);
		int letterInd;
		if (firstLetter == ' ') {
			letterInd = 26;
		} 
		else if (firstLetter == '#') {
			letterInd = 27;
		}
		else if (firstLetter == '+') {
			letterInd = 27;
		}
		else {
			letterInd = firstLetter - 'a';
		}
//		System.out.println("LETTER IND: " + letterInd + " " + firstLetter);
		// System.out.println("LETTER IND: " + letterInd);
		if (currNode.getChildren()[letterInd] == null) {
			currNode.getChildren()[letterInd] = new TrieNode(firstLetter);
			currNode.getChildren()[letterInd].setParent(currNode);
		}
		if (word.length() == 1) {
			currNode.getChildren()[letterInd].setIsWord(true);
			return;
		}
		addWordHelper(currNode.getChildren()[letterInd], word.substring(1));
	}

	/**
	 * Find the node corresponding to the last letter of a string.
	 * 
	 * @param prefix
	 *            String of which we want to find the node corresponding to the
	 *            last
	 * @return the node representing the last letter of the input word.
	 */
	public TrieNode getLastLetterNode(String prefix) {
		TrieNode currNode = root;
		for (int i = 0; i < prefix.length(); i++) {
			char currLetter = prefix.charAt(i);
			// System.out.println("CURR LETTER: " + currLetter);
			int currInd = currLetter - 'a';
			if (currNode.getChildren()[currInd] == null) {
				return null;
			}
			currNode = currNode.getChildren()[currInd];

		}
		return currNode;
	}

	/**
	 * Given a TrieNode, finds the string (word) it represents.
	 * 
	 * @param currString
	 *            current string being appended (usually initialized as the
	 *            empty string)
	 * @param currNode
	 *            current node being visited as the function traverses up the
	 *            trie
	 * @return the string represented by the node.
	 */
	public String stringFromNode(String currString, TrieNode currNode) {
		if (currNode.getParent() == null) {
			currString = currString + "";
		} else {
			String currLetter = Character.toString(currNode.getLetter());
			currString = currLetter + currString;
			return stringFromNode(currString, currNode.getParent());
		}
		return currString;
	}

	/**
	 * Checks if string exists in trie as a full word.
	 * 
	 * @param input
	 *            string to check if it exists
	 * @return boolean value indicating whether trie contains word.
	 */
	public boolean containsWord(String input) {
		TrieNode lastLetterNode = getLastLetterNode(input);
		if (lastLetterNode == null) {
			return false;
		}
		return lastLetterNode.getIsWord();
	}
}
