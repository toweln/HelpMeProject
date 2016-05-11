package edu.brown.cs.HelpMe.main;

import java.util.ArrayList;
import java.util.List;

/**
 * a class for a high-level discipline. each discipline contains several
 * subdisciplines.
 * 
 * @author andrewjones
 *
 */
public class LeaderboardEntry {

	private String username;
	private String rating;
	private String numQuestionsAnswered;

	public LeaderboardEntry(String username, String rating, String numQuestionsAnswered) {
		this.username = username;
		this.rating = rating;
		this.numQuestionsAnswered = numQuestionsAnswered;
	}

	/**
	 * gets rating
	 */
	public String getRating() {
		return rating;
	}

	/**
	 * gets username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * gets the number of questions someone has ever answered
	 */
	public String getNumQuestionsAnswered() {
		return numQuestionsAnswered;
	}

}
