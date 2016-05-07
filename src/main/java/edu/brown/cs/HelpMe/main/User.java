package edu.brown.cs.HelpMe.main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * a user on the site.
 * 
 * @author andrewjones
 *
 */
public class User {

	private String name;
	private UUID ID;
	private List<Question> submittedQuestions;
	private List<Question> answeredQuestions;
	private TagRating expertise;
	private WordCount wc;
	private String frontEndTags;
	private String username;
	private String email;

	/**
	 * initialize a user.
	 * 
	 * @param name
	 *            the user's name.
	 * @param expertise
	 *            the user's tagrating.
	 */
	public User(String name, TagRating expertise) {
		this.name = name;
		this.ID = UUID.randomUUID();
		this.submittedQuestions = new ArrayList<>();
		this.answeredQuestions = new ArrayList<>();
		this.expertise = expertise;
	}

	public User(String name, String frontEndTags, String username,
			String email) {
		this.name = name;
		this.frontEndTags = frontEndTags;
		this.username = username;
		this.email = email;
	}

	/**
	 * get the user's name.
	 * 
	 * @return the user's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * get the user's id in the database.
	 * 
	 * @return the user's id.
	 */
	public String getID() {
		return ID.toString();
	}

	/**
	 * get the list of questions this user has submitted.
	 * 
	 * @return submitted questions.
	 */
	public List<Question> getSubmittedQuestions() {
		return submittedQuestions;
	}

	/**
	 * get the list of questions this user has answered.
	 * 
	 * @return answered questoins.
	 */
	public List<Question> getAnsweredQuestions() {
		return answeredQuestions;
	}

	/**
	 * get the TagRating associated with this user's expertise.
	 * 
	 * @return the user's TagRating.
	 */
	public TagRating getExpertise() {
		return expertise;
	}

	/**
	 * add a question to the user's submitted question.
	 * 
	 * @param q
	 *            the question.
	 */
	public void addSubmittedQuestion(Question q) {
		submittedQuestions.add(q);
	}

	/**
	 * add a question to the user's answered questions.
	 * 
	 * @param q
	 *            the question.
	 */
	public void addAnsweredQuestion(Question q) {
		answeredQuestions.add(q);
	}

	/**
	 * get the user's wordcount.
	 * 
	 * @return
	 */
	public WordCount getWordCount() {
		return wc;
	}

	/**
	 * set the user's wordcount.
	 * 
	 * @param wc
	 *            the new wordcount.
	 */
	public void setWordCount(WordCount wc) {
		this.wc = wc;
	}

	public String getFrontEndTags() {
		return frontEndTags;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

}
