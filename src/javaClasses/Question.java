package edu.brown.cs.acj.helpme;

import java.util.UUID;

public class QuestionTaxonomy {

	String title;
	String message;
	UUID ID;
	UserTaxonomy owner;
	TagRating tr;
	WordCount wc;
	TagRatingTaxonomy rating;
	TagDatabaseTaxonomy td;

	/**
	 * create new question.
	 * 
	 * @param title
	 *            title of question
	 * @param message
	 *            message of question
	 * @param owner
	 *            user who asked question
	 * @param rating
	 *            TagRating of question
	 */
	public QuestionTaxonomy(String title, String message, UserTaxonomy owner,
			TagRatingTaxonomy rating) {
		this.title = title;
		this.message = message;
		this.owner = owner;
		this.ID = UUID.randomUUID();
		this.rating = rating;
		this.wc = new WordCount(message);

		// edit each tag's WordCount.
		for (Tag t : rating.getRating().keySet()) {
			t.updateWordCount(message);
			for (Tag sub : rating.getRating().keySet()) {
				sub.updateWordCount(message);
			}
		}
	}

	/**
	 * get the question's title.
	 * 
	 * @return the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * get the question's message.
	 * 
	 * @return the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * get the user who asked the question.
	 * 
	 * @return the asking user.
	 */
	public UserTaxonomy getOwner() {
		return owner;
	}

	/**
	 * get the id of the question.
	 * 
	 * @return the id.
	 */
	public String getID() {
		return ID.toString();
	}

	/**
	 * get the word count associated with the question.
	 * 
	 * @return the word count.
	 */
	public WordCount getWordCount() {
		return wc;
	}

	/**
	 * get the TagRating associated with question.
	 * 
	 * @return the TagRatign.
	 */
	public TagRatingTaxonomy getRating() {
		return rating;
	}

	/**
	 * set the WordCount of the question.
	 * 
	 * @param wc
	 *            the word count.
	 */
	public void setWordCount(WordCount wc) {
		this.wc = wc;
	}

}
