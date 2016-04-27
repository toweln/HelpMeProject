package edu.brown.cs.HelpMe.Trie;

/**
 * One node of a trie, which essentially represents a letter.
 * 
 * @author acj
 */
public class TrieNode {

	private char letter;
	private TrieNode parent;
	private TrieNode[] children;
	private boolean isWord;
	private static final int NUM_LETTERS = 29;

	/**
	 * Constructor for a trie node.
	 * 
	 * @param letter
	 *            the letter this node will represent.
	 */
	TrieNode(char letter) {
		this.letter = letter;
		this.children = new TrieNode[NUM_LETTERS];
		this.isWord = false;
	}

	/**
	 * get the letter of the node.
	 * 
	 * @return letter represented by char
	 */
	public char getLetter() {
		return letter;
	}

	/**
	 * get the parent of the node.
	 * 
	 * @return parent represented by TrieNode
	 */
	public TrieNode getParent() {
		return parent;
	}

	/**
	 * get the children of the node.
	 * 
	 * @return children represented by array
	 */
	public TrieNode[] getChildren() {
		return children;
	}

	/**
	 * find if the node represents a full word.
	 * 
	 * @return boolean representing if it's a word
	 */
	public boolean getIsWord() {
		return isWord;
	}

	/**
	 * sets whether node represents word.
	 * 
	 * @param b
	 *            true if word, false if not word
	 */
	public void setIsWord(boolean b) {
		this.isWord = b;
	}

	/**
	 * sets the parent of the node.
	 * 
	 * @param tn
	 *            node that will be the parent
	 */
	public void setParent(TrieNode tn) {
		this.parent = tn;
	}
}
