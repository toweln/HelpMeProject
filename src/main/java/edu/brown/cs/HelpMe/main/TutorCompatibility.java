package edu.brown.cs.HelpMe.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.MinMaxPriorityQueue;

/**
 * a class to assess the compatibility to between tutor/question and
 * question/tutor's past questions.
 *
 * @author andrewjones
 *
 */
public class TutorCompatibility {

	private TagDatabase td;
	private SQLQueries dbQuery;
	private Map<Question, Double> unsortedCompats;

	/**
	 * initialize a TutorCompatibility.
	 *
	 * @param td
	 *            the TagDatabase.
	 */
	public TutorCompatibility(TagDatabase td) {
		this.td = td;
		String database = "smallDb.db";
		try {
			dbQuery = new SQLQueries(database);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// public Question makeQuestion(String qID) {
	//
	// }

	/**
	 * get the compatibility of a user and a question (dot product).
	 *
	 * @param u
	 *            user.
	 * @param q
	 *            question.
	 * @return a double proportional to compatibility.
	 */
	public double tutorQuestionCompatibility(User u, Question q) {
		double dotProd = 0;
		double userLength = 0;
		double questionLength = 0;

		// for each top-level discipline...
		for (Discipline d : td.getTaxonomy()) {
			Tag top = d.getTopLevel();
			// System.out.println(top.getName());

			// for each subdiscipline within that top discipline...
			for (Tag sub : d.getSubdisciplines()) {
				double userRating = 0;
				double questionRating = 0;

				// if the rating exists, get it
				if (u.getExpertise().getRating().containsKey(top)
						&& !(u.getExpertise().getRating().get(top)
								.get(sub) == null)) {
					// System.out.println(u.getExpertise().getRating().get(top).get(sub));
					userRating = u.getExpertise().getRating().get(top).get(sub);
				}
				if (q.getRating().getRating().containsKey(top) && !(q
						.getRating().getRating().get(top).get(sub) == null)) {
					questionRating = q.getRating().getRating().get(top)
							.get(sub);
				}

				// increment dot product and lengths
				dotProd += (userRating * questionRating);
				userLength += Math.pow(userRating, 2);
				questionLength += Math.pow(questionRating, 2);
			}
		}

		// find distance between vectors and find similarity
		double vectorDist = Math.pow(userLength, 0.5)
				* Math.pow(questionLength, 0.5);
		return Math.round((dotProd / vectorDist) * 1000.0) / 1000.0;
	}

	public double tutorQuestionCompatibility(String userID, String qID)
			throws SQLException {
		// System.out.println(qID);
		double dotProd = 0;
		double userLength = 0;
		double questionLength = 0;

		User u = dbQuery.makeUser(userID);
		Question q = dbQuery.makeQuestion(qID);

		// for each top-level discipline...
		for (Discipline d : td.getTaxonomy()) {
			Tag top = d.getTopLevel();
			// System.out.println(top.getName());

			// for each subdiscipline within that top discipline...
			for (Tag sub : d.getSubdisciplines()) {
				double userRating = 0;
				double questionRating = 0;

				// if the rating exists, get it
				if (u.getExpertise().getRating().containsKey(top)
						&& !(u.getExpertise().getRating().get(top)
								.get(sub) == null)) {
					// System.out.println(u.getExpertise().getRating().get(top).get(sub));
					userRating = u.getExpertise().getRating().get(top).get(sub);
				}
				if (q.getRating().getRating().containsKey(top) && !(q
						.getRating().getRating().get(top).get(sub) == null)) {
					questionRating = q.getRating().getRating().get(top)
							.get(sub);
					// System.out.println(" " + sub.getName());
				}

				// increment dot product and lengths
				dotProd += (userRating * questionRating);
				userLength += Math.pow(userRating, 2);
				questionLength += Math.pow(questionRating, 2);
			}
		}

		// find distance between vectors and find similarity
		double vectorDist = Math.pow(userLength, 0.5)
				* Math.pow(questionLength, 0.5);
		// System.out.println(" DOT PROD: " + dotProd);
		// System.out.println(" DIST: " + vectorDist);
		return Math.round((dotProd / vectorDist) * 1000.0) / 1000.0;
	}

	/**
	 * find compatibility between the current question and questions the user
	 * has answered in the past.
	 *
	 * @param u
	 *            user.
	 * @param q
	 *            question.
	 * @return a double proportional to compatibility.
	 */
	public double currentPastQuestionCompatibility(User u, Question q) {
		Map<String, Integer> questionCount = q.getWordCount()
				.getUnigramCounts();
		Map<String, Integer> userCount = u.getWordCount().getUnigramCounts();
		double dotProd = 0;
		double userLength = 0;
		double questionLength = 0;
		for (String word : userCount.keySet()) {
			userLength += Math.pow(userCount.get(word), 2);
		}
		for (String word : questionCount.keySet()) {
			questionLength += Math.pow(questionCount.get(word), 2);
			if (userCount.keySet().contains(word)) {
				dotProd += questionCount.get(word) * userCount.get(word);
			} else {
				continue;
			}
		}
		double vectorDist = Math.pow(userLength, 0.5)
				* Math.pow(questionLength, 0.5);
		return Math.round((dotProd / vectorDist) * 1000.0) / 1000.0;
	}

	public double currentPastQuestionCompatibility(String userID, String qID)
			throws SQLException {
		Question q = dbQuery.makeQuestion(qID);
		User u = dbQuery.makeUser(userID);
		// System.out.println(u.getName());
		WordCount wc = dbQuery.getUserWordCount(userID);
		u.setWordCount(wc);
		Map<String, Integer> questionCount = q.getWordCount()
				.getUnigramCounts();
		Map<String, Integer> userCount = u.getWordCount().getUnigramCounts();
		double dotProd = 0;
		double userLength = 0;
		double questionLength = 0;
		for (String word : userCount.keySet()) {
			userLength += Math.pow(userCount.get(word), 2);
		}
		for (String word : questionCount.keySet()) {
			questionLength += Math.pow(questionCount.get(word), 2);
			if (userCount.keySet().contains(word)) {
				dotProd += questionCount.get(word) * userCount.get(word);
			} else {
				continue;
			}
		}
		double vectorDist = Math.pow(userLength, 0.5)
				* Math.pow(questionLength, 0.5);
		return Math.round((dotProd / vectorDist) * 1000.0) / 1000.0;
	}

	/**
	 * get the sum of both types of compatibility.
	 *
	 * @param u
	 *            the user.
	 * @param q
	 *            the question.
	 * @return the compatibility.
	 */
	public double getOverallCompatibility(User u, Question q) {
		double compat = tutorQuestionCompatibility(u, q)
				+ currentPastQuestionCompatibility(u, q);
		return Math.round(compat * 1000.0) / 1000.0;
	}

	public double getOverallCompatibility(String userID, String qID)
			throws SQLException {
		// System.out.println(qID);
		// System.out.println(" TQ: " + tutorQuestionCompatibility(userID,
		// qID));
		// System.out.println(
		// " CPQ: " + currentPastQuestionCompatibility(userID, qID));
		double compat = tutorQuestionCompatibility(userID, qID)
				+ currentPastQuestionCompatibility(userID, qID);
		// System.out.println(" OVERALL: " + compat);

		return Math.round(compat * 1000.0) / 1000.0;
	}

	/**
	 * sort the map of questions and compatibilities.
	 *
	 * @param unsortMap
	 *            the unsorted map.
	 * @return the sorted map.
	 */
	private static Map<Question, Double> sortByComparator(
			Map<Question, Double> unsortMap) {

		// Convert Map to List
		List<Map.Entry<Question, Double>> list = new LinkedList<Map.Entry<Question, Double>>(
				unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Question, Double>>() {
			@Override
			public int compare(Map.Entry<Question, Double> o1,
					Map.Entry<Question, Double> o2) {
				// System.out.println(o2.getKey().getMessage());
				// System.out.println(o2.getValue());
				// System.out.println(o1.getValue());
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// Convert sorted map back to a Map
		Map<Question, Double> sortedMap = new LinkedHashMap<Question, Double>();
		for (Iterator<Map.Entry<Question, Double>> it = list.iterator(); it
				.hasNext();) {
			Map.Entry<Question, Double> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/**
	 * given multiple questions, rank the tutor's compatibility with each and
	 * sort.
	 *
	 * @param u
	 *            the user.
	 * @param qs
	 *            list of questions.
	 * @return a sorted map of questions and compatibilities.
	 */
	public Map<Question, Double> rankCompatibilities(User u,
			List<Question> qs) {
		this.unsortedCompats = new HashMap<>();
		for (Question q : qs) {
			double currCompat = getOverallCompatibility(u, q);
			unsortedCompats.put(q, currCompat);
		}
		Map<Question, Double> sortedCompats = sortByComparator(unsortedCompats);
		return sortedCompats;
	}

	public List<Question> getSortedQuestions(String userID)
			throws SQLException {
		MinMaxPriorityQueue<Question> questions = MinMaxPriorityQueue
				.orderedBy(new QuestionComparator()).create();
		this.unsortedCompats = new HashMap<>();
		List<String> qIDs = dbQuery.getAllQIDs();
		// System.out.println(qIDs);
		for (String qID : qIDs) {
			Question q = dbQuery.makeQuestion(qID);
			double currCompat = getOverallCompatibility(userID, qID);
			unsortedCompats.put(q, currCompat);
			System.out.println(q.getMessage());
			System.out.println(currCompat);
			System.out.println(q.getFrontEndTags());
			if (!q.getOwnerID().equals(userID) && q.getTutor().equals("")) {
				questions.add(q);
			}
		}
		List<Question> sortedQs = new ArrayList<>();
		while (!questions.isEmpty()) {
			sortedQs.add(questions.pollFirst());
		}
		return sortedQs;
	}

	/**
	 * Path comparator.
	 * 
	 * @author jplee
	 */
	private class QuestionComparator implements Comparator<Question> {
		@Override
		public int compare(Question q1, Question q2) {
			return unsortedCompats.get(q2).compareTo(unsortedCompats.get(q1));
		}
	}
}
