package edu.brown.cs.HelpMe.main;

import java.util.List;
import java.util.UUID;

/**
 * a class for a question submitted on the site.
 *
 * @author andrewjones
 *
 */
public class Question {

	private String title;
	private String message;
	private String ID;
	private User owner;
	private WordCount wc;
	private TagRating rating;
	private TagDatabase td;
	private List<String> frontEndTags;
	private String latitude;
	private String longitude;
	private String ownerID;
	private String tutor;

	/**
	 * create new question.
	 *
	 * @param title
	 *            title of question
	 * @param message
	 *            message of question
	 * @param rating
	 *            TagRating of question
	 */

	public Question(String title, String message, User owner, TagRating rating,
			TagDatabase td) {
		this.title = title;
		this.message = message;
		this.owner = owner;
		this.ID = UUID.randomUUID().toString();
		this.rating = rating;
		this.wc = new WordCount(message);
		this.td = td;

		for (Discipline d : td.getTaxonomy()) {
			if (rating.getRating().containsKey(d)) {
				for (Tag t : d.getSubdisciplines()) {
					if (rating.getRating().get(d).containsKey(t)) {
						t.updateWordCount(message);
					}
				}
			}
		}
	}

	public Question(String ID, String title, String message, TagRating rating,
			TagDatabase td, List<String> frontEndTags, String lat, String lon,
			String ownerID, String tutor) {
		this.title = title;
		this.message = message;
		this.ID = ID;
		this.rating = rating;
		this.wc = new WordCount(message);
		this.td = td;
		this.frontEndTags = frontEndTags;
		this.latitude = lat;
		this.longitude = lon;
		this.ownerID = ownerID;
		this.tutor = tutor;
		for (Discipline d : td.getTaxonomy()) {
			if (rating.getRating().containsKey(d)) {
				for (Tag t : d.getSubdisciplines()) {
					if (rating.getRating().get(d).containsKey(t)) {
						t.updateWordCount(message);
					}
				}
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
	public User getOwner() {
		return owner;
	}

	/**
	 * get the id of the question.
	 *
	 * @return the id.
	 */
	public String getID() {
		return ID;
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
	public TagRating getRating() {
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

	public List<String> getFrontEndTags() {
		return frontEndTags;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the ownerID
	 */
	public String getOwnerID() {
		return ownerID;
	}

	/**
	 * @param ownerID
	 *            the ownerID to set
	 */
	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	/**
	 * @return the tutor
	 */
	public String getTutor() {
		return tutor;
	}

	/**
	 * @param tutor
	 *            the tutor to set
	 */
	public void setTutor(String tutor) {
		this.tutor = tutor;
	}

}
