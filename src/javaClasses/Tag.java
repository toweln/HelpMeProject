package edu.brown.cs.acj.helpme;

/**
 * a class for a tag of a question or user's expertise. questions and users can
 * have multiple tags.
 * 
 * @author andrewjones
 *
 */
public class Tag {

	private String name;
	private WordCount wc;

	/**
	 * initialize a tag.
	 * @param name the tag's name.
	 */
	public Tag(String name) {
		this.name = name;
		this.wc = new WordCount();
	}

	/**
	 * get the tag's name.
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * get the wordcount associated with the tag.
	 * @return the wordcount.
	 */
	public WordCount getWordCount() {
		return wc;
	}

	/**
	 * update the tag's wordcount with a new message.
	 * @param message the new message.
	 */
	public void updateWordCount(String message) {
		message = message.toLowerCase().replaceAll("[^a-zA-Z ]", " ");
		String[] words = message.split("\\s+");
		// System.out.println("UPDATING " + name);
		// System.out.println(" message: " + message);
		for (String s : words) {
			s = s.replaceAll("\\s+", "");
			if (s.equals("")) {
				continue;
			}
			wc.addNumWord();
			if (wc.getUnigramCounts().containsKey(s)) {
				int uniCount = wc.getUnigramCounts().get(s);
				wc.getUnigramCounts().put(s, uniCount + 1);
			} else {
				wc.getUnigramCounts().put(s, 1);
			}
		}
		// System.out.println(" new unigram: " + wc.getUnigramCounts());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Tag) {
			Tag other = (Tag) o;
			return (getName().equals(other.getName()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (name.hashCode());
	}

}
