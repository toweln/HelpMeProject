package edu.brown.cs.HelpMe.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.HelpMe.email.UserData;

/**
 * SQLQueries class. Handles calling queries which are used in other areas of
 * helpMe.
 *
 * @author jplee
 */
public class SQLQueries {
	/**
	 * The connection.
	 */
	private Connection conn;
	private List<String> mathDisc;
	private List<String> sciDisc;
	private List<String> humDisc;
	private List<String> csDisc;
	private List<String> histDisc;
	private List<String> langDisc;
	private Map<String, String> discMap;
	private List<String> allDisc;
	private Map<String, Map<String, Integer>> existingCounts;

	/**
	 * SQLQueries constructor that handles setting up the connection to the
	 * database. Gets data from the db which is used by the other classes.
	 *
	 * @param db
	 *            Path to database file.
	 * @throws ClassNotFoundException
	 *             invalid db file.
	 * @throws SQLException
	 *             SQL error.
	 */
	public SQLQueries(String db) throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + db);
		// conn.setAutoCommit(false);
		existingCounts = new HashMap<>();
		conn.createStatement();
		mathDisc = new ArrayList<String>();
		sciDisc = new ArrayList<String>();
		humDisc = new ArrayList<String>();
		csDisc = new ArrayList<String>();
		histDisc = new ArrayList<String>();
		langDisc = new ArrayList<String>();

		discMap = new HashMap<>();
		allDisc = new ArrayList<>();

		mathDisc.add("Algebra");
		mathDisc.add("Statistics");
		mathDisc.add("Probability");
		mathDisc.add("Calculus");
		mathDisc.add("Multivariable Calculus");
		mathDisc.add("Differential Equations");
		mathDisc.add("Discrete Math");
		mathDisc.add("Geometry");
		mathDisc.add("Number Theory");
		mathDisc.add("Set Theory");
		mathDisc.add("Linear Algebra");

		for (String mathTag : mathDisc) {
			discMap.put(mathTag, "Math");
			allDisc.add(mathTag);
		}

		sciDisc.add("Biology");
		sciDisc.add("Microbiology");
		sciDisc.add("Nutrition");
		sciDisc.add("Physiology");
		sciDisc.add("Plant Biology");
		sciDisc.add("Chemistry");
		sciDisc.add("Inorganic Chemistry");
		sciDisc.add("Organic Chemistry");
		sciDisc.add("Physics");
		sciDisc.add("Biochemistry");
		sciDisc.add("Geology");
		sciDisc.add("Materials Science");

		for (String sciTag : sciDisc) {
			discMap.put(sciTag, "Science");
			allDisc.add(sciTag);
		}

		humDisc.add("Communication");
		humDisc.add("Dance");
		humDisc.add("Education");
		humDisc.add("Graphic Design");
		humDisc.add("Film and Theater");
		humDisc.add("Linguistics");
		humDisc.add("Music");
		humDisc.add("Philosophy");
		humDisc.add("Art");
		humDisc.add("Art History");
		humDisc.add("English");
		humDisc.add("Ethnic Studies");
		humDisc.add("Gender Studies");
		humDisc.add("Music Theory");
		humDisc.add("Religious Studies");
		humDisc.add("Writing");

		for (String humTag : humDisc) {
			discMap.put(humTag, "Humanities");
			allDisc.add(humTag);
		}

		csDisc.add("Artificial Intelligence");
		csDisc.add("C");
		csDisc.add("C#");
		csDisc.add("HTML");
		csDisc.add("Javascript");
		csDisc.add("jQuery");
		csDisc.add("Machine Learning");
		csDisc.add("PHP");
		csDisc.add("Python");
		csDisc.add("Scala");
		csDisc.add("Visual Basic");
		csDisc.add("Arduino");
		csDisc.add("Computer Graphics");
		csDisc.add("C++");
		csDisc.add("CSS");
		csDisc.add("Java");
		csDisc.add("Lisp");
		csDisc.add("Perl");
		csDisc.add("Ruby");
		csDisc.add("MATLAB");
		csDisc.add("R");
		csDisc.add("SAS");
		csDisc.add("SQL");
		csDisc.add("Stata");

		for (String csTag : csDisc) {
			discMap.put(csTag, "Computer Science and Technology");
			allDisc.add(csTag);
		}

		histDisc.add("African History");
		histDisc.add("European History");
		histDisc.add("US History");
		histDisc.add("East Asian History");
		histDisc.add("Social History");
		histDisc.add("World History");

		for (String histTag : histDisc) {
			discMap.put(histTag, "History");
			allDisc.add(histTag);
		}

		langDisc.add("American Sign Language");
		langDisc.add("Arabic");
		langDisc.add("Cantonese");
		langDisc.add("Dutch");
		langDisc.add("French");
		langDisc.add("German");
		langDisc.add("Hebrew");
		langDisc.add("Hindi");
		langDisc.add("Italian");
		langDisc.add("Japanese");
		langDisc.add("Korean");
		langDisc.add("Latin");
		langDisc.add("Mandarin");
		langDisc.add("Portuguese");
		langDisc.add("Russian");
		langDisc.add("Spanish");
		langDisc.add("Swahili");

		for (String langTag : langDisc) {
			discMap.put(langTag, "Languages");
			allDisc.add(langTag);
		}
	}

	/**
	 * Given an String representation of a req's tags, return the list of
	 * strings which are the actual tags in words.
	 *
	 * @param discipline
	 *            Whichever discipline the request belongs to.
	 * @param tagInfo
	 *            The string representation of the tags.
	 * @return List of strings which are the actual tags.
	 */
	public List<String> getTagsFromString(String discipline, String tagInfo) {
		Double binaryString = Double.parseDouble(tagInfo);
		List<String> ret = new ArrayList<String>();
		ret = mathDisciplineHelper(discipline, binaryString);

		return ret;
	}

	/**
	 * Helper method for getTagsFromString.
	 *
	 * @param disc
	 *            Discipline the request belongs to
	 * @param tags
	 *            The integer representation of the tags
	 * @return List of strings which are the literal tags.
	 */
	public List<String> mathDisciplineHelper(String disc, Double tags) {
		List<String> ret = new ArrayList<String>();
		List<String> discList = new ArrayList<String>();
		double tags2 = tags;
		int increment = 0;
		if (disc.equals("Math")) {
			discList = mathDisc;
		} else if (disc.equals("Science")) {
			discList = sciDisc;
		} else if (disc.equals("Humanities")) {
			discList = humDisc;
		} else if (disc.equals("CSTech")) {
			discList = csDisc;
		}
		while (tags2 > 0) {
			// Check whether the first digit is 0/1
			if (tags2 % 10 != 0) {
				ret.add(discList.get(increment));
			}
			increment++;
			tags2 = Math.floor(tags2 / 10);
		}
		return ret;
	}

	/**
	 * Converts a list of tags into a string of taginfo.
	 *
	 * @param disc
	 *            the discipline the request falls under
	 * @param tags
	 *            the list of tags in literal words
	 * @return A string which can be inserted into the db.
	 */
	public String tagsToString(String disc, List<String> tags) {
		double ret = 0;
		List<String> discList = new ArrayList<String>();
		if (disc.equals("Math")) {
			discList = mathDisc;
		} else if (disc.equals("Science")) {
			discList = sciDisc;
		} else if (disc.equals("Humanities")) {
			discList = humDisc;
		} else if (disc.equals("CSTech")) {
			discList = csDisc;
		}
		for (String s : tags) {
			int index = discList.indexOf(s);
			ret = ret + Math.pow(10, index);
		}
		return Double.toString(ret);
	}

	public Boolean emailUnique(String email) throws SQLException {
		String query = "SELECT COUNT(*) FROM users WHERE users.email_address = ?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, email);
		ResultSet results = stat.executeQuery();
		int ret = results.getInt(1);
		// System.out.println("ret");
		// System.out.println(ret);
		if (ret != 0) {
			return false;
		}
		return true;
	}

	public Boolean userUnique(String user) throws SQLException {
		String query = "SELECT COUNT(*) FROM users WHERE users.username = ?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, user);
		ResultSet results = stat.executeQuery();
		int ret = results.getInt(1);
		// System.out.println("ret");
		// System.out.println(ret);
		if (ret != 0) {
			return false;
		}
		return true;
	}

	public Boolean insertNewUser(String userid, String first, String last,
			String email, String phoneNumber, String userName, String pw)
			throws SQLException {
		if (!emailUnique(email)) {
			return false;
		}
		if (!userUnique(userName)) {
			return false;
		}
		String query = "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stat = conn.prepareStatement(query);
		List<String> toprint = new ArrayList<String>();
		toprint.add(userName);
		toprint.add(pw);
		toprint.add(first);
		toprint.add(last);
		toprint.add(email);
		toprint.add(phoneNumber);
		for (int i = 0; i < toprint.size(); i++) {
			System.out.println(toprint.get(i));
		}
		stat.setString(1, userid);
		stat.setString(2, first);
		stat.setString(3, last);
		stat.setString(4, email);
		stat.setString(5, phoneNumber);
		stat.setString(6, userName);
		stat.setString(7, pw);
		int test = stat.executeUpdate();
		// conn.commit();
		System.out.println(test);
		return true;
	}

	public void insertUserExpertise(List<String> topics, String userID)
			throws SQLException {

		String query = "INSERT INTO tags ";
		// System.out.println("TAGS QUERY: " + query);
		StringBuilder sbTags = new StringBuilder();
		StringBuilder sbRatings = new StringBuilder();
		sbTags.append("(user_id, ");
		sbRatings.append("('" + userID + "',");
		for (String t : topics) {
			sbTags.append(t);
			sbTags.append(",");
			sbRatings.append("1");
			sbRatings.append(",");
		}
		String tagList = sbTags.toString();
		tagList = tagList.substring(0, tagList.length() - 1);
		tagList += ")";

		String ratingsList = sbRatings.toString();
		ratingsList = ratingsList.substring(0, ratingsList.length() - 1);
		ratingsList += ")";

		query += (tagList + " VALUES " + ratingsList);
		System.out.println(query);
		PreparedStatement stat = conn.prepareStatement(query);
		stat.executeUpdate();
	}

	/**
	 * form a question object from the request's id.
	 *
	 * @param qID
	 *            the request id.
	 * @return the question object.
	 * @throws SQLException
	 *             if the databse doesn't exist.
	 */
	public Question makeQuestion(String qID) throws SQLException {
		String query = "SELECT body, tags, summary FROM requests WHERE request_id = ?;";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, qID);
		// System.out.println("ID: " + qID);
		ResultSet rs = stat.executeQuery();
		String body = "";
		String tags = "";
		String title = "";
		while (rs.next()) {
			body = rs.getString(1);
			tags = rs.getString(2);
			title = rs.getString(3);
			// System.out.println("TAGS: " + tags);
			// System.out.println("BODY: " + body);
		}

		rs.close();
		// List<String> qBodyWords = Arrays.asList(body.split("\\s+"));
		List<String> qTags = Arrays.asList(tags.split("\\s*,\\s*"));
		Map<String, Map<String, Double>> qoverallRating = new HashMap<>();
		List<String> frontEndTags = new ArrayList<>();
		for (String tag : qTags) {
			if (tag.equals("")) {
				continue;
			}
			tag = tag.replaceAll("^\"|\"$", "");
			String[] splitStr = tag.split("_");
			StringBuilder sb = new StringBuilder();
			for (String s : splitStr) {
				// System.out.println(s);
				sb.append(s.substring(0, 1).toUpperCase() + s.substring(1));
				sb.append(" ");
			}
			String finalTag = sb.toString();
			tag = finalTag.substring(0, finalTag.length() - 1);
			// System.out.println(tag);
			String currDisc = discMap.get(tag);
			if (qoverallRating.containsKey(currDisc)) {
				qoverallRating.get(currDisc).put(tag, 1.0);
			} else {
				Map<String, Double> newDiscMap = new HashMap<>();
				qoverallRating.put(currDisc, newDiscMap);
				qoverallRating.get(currDisc).put(tag, 1.0);
			}
			frontEndTags.add(tag);
		}
		// System.out.println(qoverallRating);
		TagDatabase td = new TagDatabase();
		TagRating trq = new TagRating(qoverallRating, td);
		Question q = new Question(qID, title, body, trq, td, frontEndTags);
		return q;
	}

	public void setExistingWC(Map<String, Map<String, Integer>> ex) {
		this.existingCounts = ex;
	}

	// public void updateExistingWC(String word) {
	// int currCount = 0;
	// if (existingCounts.containsKey(word)) {
	// currCount = existingCounts.get(word);
	// }
	// existingCounts.put(word, currCount + 1);
	// }

	public String getTuteeFromReqId(String reqid) throws SQLException {
		String query = "SELECT tutee_id FROM requests WHERE request_id=?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, reqid);
		ResultSet results = stat.executeQuery();
		String ret = results.getString(1);
		return ret;
	}

	public String getTutorFromReqId(String reqid) throws SQLException {
		String query = "SELECT tutor_id FROM requests WHERE request_id=?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, reqid);
		ResultSet results = stat.executeQuery();
		String ret = results.getString(1);
		return ret;
	}

	public UserData getUserDataFromId(String userId) throws SQLException {
		String query = "SELECT * FROM users WHERE user_id=?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, userId);
		ResultSet results = stat.executeQuery();
		UserData user = null;
		while (results.next()) {
			user = new UserData(results.getString(1), results.getString(2),
					results.getString(3), results.getString(4),
					results.getString(5), results.getString(6));
		}

		return user;
	}

	public String getRequestSummary(String reqid) throws SQLException {
		String query = "SELECT summary FROM requests WHERE request_id=?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, reqid);
		ResultSet results = stat.executeQuery();
		String summary = results.getString(1);
		return summary;
	}

	public void updateWordCount(List<String> tags, String body)
			throws SQLException {
		String[] words = body.split("\\s+");
		Map<String, Map<String, Integer>> updateCounts = new HashMap<>();
		Map<String, Map<String, Integer>> newCounts = new HashMap<>();
		for (String t : tags) {
			Map<String, Integer> currUpdateMap = new HashMap<>();
			Map<String, Integer> currNewMap = new HashMap<>();
			updateCounts.put(t, currUpdateMap);
			updateCounts.put(t, currNewMap);
			for (String w : words) {
				if (existingCounts.get(t).containsKey(w)) {
					int currCount = 0;
					if (updateCounts.get(t).containsKey(w)) {
						currCount = updateCounts.get(t).get(w);
					}
					updateCounts.get(t).put(w, currCount + 1);
				} else {
					int currCount = 0;
					if (newCounts.get(t).containsKey(w)) {
						currCount = newCounts.get(t).get(w);
					}
					newCounts.get(t).put(w, currCount + 1);
				}
			}
		}

		String updateQuery = "UPDATE tag_wordcounts SET count = ? where (tag = ? and word = ?);";
		PreparedStatement stat1 = conn.prepareStatement(updateQuery);
		for (String t : updateCounts.keySet()) {
			for (String w : updateCounts.get(t).keySet()) {
				stat1.setInt(1, updateCounts.get(t).get(w));
				stat1.setString(2, t);
				stat1.setString(3, w);
				stat1.executeUpdate();
			}
		}

		String insertQuery = "INSERT into tag_wordcounts (tag, word, count) VALUES (?, ?, ?);";
		PreparedStatement stat2 = conn.prepareStatement(insertQuery);
		for (String t : newCounts.keySet()) {
			for (String w : newCounts.get(t).keySet()) {
				stat2.setString(1, t);
				stat2.setString(2, w);
				stat2.setInt(3, newCounts.get(t).get(w));
				stat2.executeUpdate();
			}
		}
	}

	public void initializeExistingCounts() throws SQLException {

		String query = "SELECT body, tags FROM requests;";
		PreparedStatement stat = conn.prepareStatement(query);
		ResultSet rs = stat.executeQuery();
		Map<String, Map<String, Integer>> updateWC = new HashMap<>();
		for (String disc : allDisc) {
			Map<String, Integer> currMap = new HashMap<>();
			updateWC.put(disc, currMap);
		}

		while (rs.next()) {
			String message = rs.getString(1);
			if (rs.getString(2) == null) {
				continue;
			}
			String[] tags = rs.getString(2).split("\\s*,\\s*");
			// System.out.println(tags);
			if (tags[0].equals("")) {
				continue;
			}

			String[] words = message.split("\\s+");
			for (String t : tags) {
				// System.out.println(t);
				// t.substring(0, 1).toUpperCase() + t.substring(1));
				t = t.replace("\"", "");
				String[] splitStr = t.split("_");
				StringBuilder sb = new StringBuilder();
				for (String s : splitStr) {
					// System.out.println(s);
					sb.append(s.substring(0, 1).toUpperCase() + s.substring(1));
					sb.append(" ");
				}
				String finalTag = sb.toString();
				t = finalTag.substring(0, finalTag.length() - 1);

				if (t.equals("Html")) {
					t = "HTML";
				}
				if (t.equals("Matlab")) {
					t = "MATLAB";
				}
				if (t.equals("Sas")) {
					t = "SAS";
				}
				if (t.equals("Css")) {
					t = "CSS";
				}
				if (t.equals("Jquery")) {
					t = "jQuery";
				}
				if (t.equals("Sql")) {
					t = "SQL";
				}
				if (t.equals("Php")) {
					t = "PHP";
				}
				if (t.equals("Us History")) {
					t = "US History";
				}
				// System.out.println(t);
				for (String w : words) {

					int currCount = 0;
					if (updateWC.get(t).containsKey(w)) {
						currCount = updateWC.get(t).get(w);
					}
					updateWC.get(t).put(w, currCount + 1);
				}
			}
		}
		// System.out.println(updateWC);
		setExistingWC(updateWC);
	}

	public void initializeWordcountDB() throws SQLException {

		String query = "SELECT body, tags FROM requests;";
		PreparedStatement stat = conn.prepareStatement(query);
		ResultSet rs = stat.executeQuery();
		Map<String, Map<String, Integer>> updateWC = new HashMap<>();
		for (String disc : allDisc) {
			Map<String, Integer> currMap = new HashMap<>();
			updateWC.put(disc, currMap);
		}

		while (rs.next()) {
			String message = rs.getString(1);
			if (rs.getString(2) == null) {
				continue;
			}
			String[] tags = rs.getString(2).split("\\s*,\\s*");
			if (tags[0].equals("")) {
				continue;
			}

			String[] words = message.split("\\s+");
			for (String t : tags) {
				// System.out.println(
				// t.substring(0, 1).toUpperCase() + t.substring(1));
				t = t.substring(0, 1).toUpperCase() + t.substring(1);
				for (String w : words) {
					int currCount = 0;
					if (updateWC.get(t).containsKey(w)) {
						currCount = updateWC.get(t).get(w);
					}
					updateWC.get(t).put(w, currCount + 1);
				}
			}
		}
		// System.out.println(updateWC);

		String updateWCquery = "INSERT into tag_wordcounts (tag, word, count) VALUES (?, ?, ?);";
		PreparedStatement statUpdate = conn.prepareStatement(updateWCquery);
		for (String t : updateWC.keySet()) {
			for (String w : updateWC.get(t).keySet()) {
				statUpdate.setString(1, t);
				statUpdate.setString(2, w);
				statUpdate.setInt(3, updateWC.get(t).get(w));
				statUpdate.executeUpdate();
			}

		}
		setExistingWC(updateWC);
		// System.out.println(existingCounts);

	}

	public User makeUser(String userID) throws SQLException {
		String query = "SELECT * FROM tags WHERE user_id = ?;";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, userID);
		ResultSet rs = stat.executeQuery();
		// String body = "";
		Map<String, Map<String, Double>> ratingMap = new HashMap<>();
		while (rs.next()) {
			for (int i = 0; i < allDisc.size() - 6; i++) {
				String currTag = allDisc.get(i);
				String currRating = rs.getString(i + 2);
				if (currRating != null) {
					// System.out.println(currDisc + " " + currRating);
					Double numRating = Double.parseDouble(currRating);
					String currDisc = discMap.get(currTag);
					if (ratingMap.containsKey(currDisc)) {
						ratingMap.get(currDisc).put(currDisc, numRating);
					} else {
						Map<String, Double> newDiscMap = new HashMap<>();
						ratingMap.put(currDisc, newDiscMap);
						ratingMap.get(currDisc).put(currTag, numRating);
					}
				}
			}
		}
		// System.out.println(ratingMap);
		TagDatabase td = new TagDatabase();
		TagRating tru = new TagRating(ratingMap, td);
		User u = new User("", tru);
		return u;
	}

	public User makeUserProfile(String userID) throws SQLException {
		String query1 = "SELECT * FROM tags WHERE user_id = ?;";
		PreparedStatement stat1 = conn.prepareStatement(query1);
		stat1.setString(1, userID);
		ResultSet rs1 = stat1.executeQuery();
		List<String> frontEndTags = new ArrayList<>();
		// String body = "";
		while (rs1.next()) {
			for (int i = 0; i < allDisc.size() - 6; i++) {
				String currTag = allDisc.get(i);
				String currRating = rs1.getString(i + 2);
				if (currRating != null) {
					frontEndTags.add(currTag);
				}
			}
		}
		String tagList = "";
		if (frontEndTags.isEmpty()) {
			tagList = "";
		} else {
			StringBuilder sb = new StringBuilder();
			for (String t : frontEndTags) {
				sb.append(t);
				sb.append(", ");
			}
			tagList = sb.toString();
			tagList = tagList.substring(0, tagList.length() - 2);
		}
		String query2 = "SELECT first_name, last_name, email_address, username FROM users WHERE user_id = ?";
		// System.out.println(query2);
		PreparedStatement stat2 = conn.prepareStatement(query2);
		stat2.setString(1, userID);
		ResultSet rs2 = stat2.executeQuery();

		String firstname = "";
		String lastname = "";
		String name = "";
		String email_address = "";
		String username = "";
		while (rs2.next()) {
			firstname = rs2.getString(1);
			lastname = rs2.getString(2);
			name = firstname + " " + lastname;
			email_address = rs2.getString(3);
			username = rs2.getString(4);
		}

		// System.out.println(name);
		// System.out.println(email_address);
		// System.out.println(username);
		// System.out.println(tagList);

		User u = new User(name, tagList, username, email_address);
		return u;
	}

	public WordCount getUserWordCount(String userID) throws SQLException {
		WordCount wc = new WordCount();
		String query = "SELECT body from requests where tutor_id = ?;";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, userID);

		ResultSet rs = stat.executeQuery();
		while (rs.next()) {
			// System.out.println(rs.getString(1));
			wc.updateWordCount(rs.getString(1));
		}
		return wc;
		// System.out.println(wc.getUnigramCounts());
	}

	public void insertNewRequest(String reqid, String tuteeid, String tutorid,
			String disc, List<String> tags, String summary, String body,
			String postLat, String postLon, String timePosted,
			String timeResponded, String rating) throws SQLException {
		String query = "INSERT INTO requests VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, reqid);
		stat.setString(2, tuteeid);
		stat.setString(3, tutorid);

		stat.setString(4, disc);
		// Still need to figure out what's going on with tags and stuff
		// String nTags = this.tagsToString(disc, tags);
		StringBuilder sb = new StringBuilder();
		for (String t : tags) {
			sb.append(t + ",");
		}
		String tagsCommaList = sb.toString();
		tagsCommaList = tagsCommaList.substring(0, tagsCommaList.length() - 1);
		System.out.println("TAGS COMMA LIST: " + tagsCommaList);
		stat.setString(5, tagsCommaList);
		stat.setString(6, summary);
		stat.setString(7, body);
		stat.setString(8, postLat);
		stat.setString(9, postLon);
		stat.setString(10, timePosted);
		stat.setString(11, timeResponded);
		stat.setString(12, rating);
		stat.executeUpdate();
	}

	public List<LeaderboardEntry> getOrderedLeaderboard() throws SQLException {
		String query = "select * from leaderboard order by average_rating desc limit 5;";
		PreparedStatement stat = conn.prepareStatement(query);
		ResultSet rs = stat.executeQuery();
		List<LeaderboardEntry> leaders = new ArrayList<>();
		while (rs.next()) {
			String currUser = rs.getString(2);
			String numQuestionsAnswered = rs.getString(4);
			String currRating = rs.getString(5);
			leaders.add(new LeaderboardEntry(currUser, currRating,
					numQuestionsAnswered));
		}
		return leaders;
	}

	/**
	 * get all ids of all current questions in the db.
	 *
	 * @return list of strings for all ids of questions.
	 * @throws SQLException
	 *             if the database doesn't exist.
	 */
	public List<String> getAllQIDs() throws SQLException {
		String query = "select request_id from requests;";
		PreparedStatement stat = conn.prepareStatement(query);
		ResultSet rs = stat.executeQuery();
		List<String> qIDs = new ArrayList<>();
		while (rs.next()) {
			qIDs.add(rs.getString(1));
		}
		return qIDs;
	}

	public void updateRequestBody(String reqid, String body)
			throws SQLException {
		String query = "UPDATE requests SET body=? WHERE request_id=?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, body);
		stat.setString(2, reqid);
		stat.executeQuery();
	}

	public void updateRequestTutor(String reqid, String tutor)
			throws SQLException {
		reqid = "\"" + reqid + "\"";
		tutor = "\"" + tutor + "\"";
		String query = "UPDATE requests SET tutor_id=? WHERE request_id=?";
		PreparedStatement stat = conn.prepareStatement(query);
		System.out.println("TUTOR ID: " + tutor);
		System.out.println("REQUEST ID: " + reqid);
		stat.setString(1, tutor);
		stat.setString(2, reqid);
		stat.executeUpdate();
	}

	public void updateTimeResponded(String timeRes, String reqid)
			throws SQLException {
		String query = "UPDATE requests SET time_responded=? WHERE request_id=?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, timeRes);
		stat.setString(2, reqid);
		stat.executeUpdate();
	}

	public void updateRating(String rating, String reqid) throws SQLException {
		String query = "UPDATE requests SET rating=? WHERE request_id=?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, rating);
		stat.setString(2, reqid);
		stat.executeQuery();
	}

	public String getPasswordFromEmail(String email) throws SQLException {
		String query = "SELECT password FROM users WHERE email_address=?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, email);
		ResultSet results = stat.executeQuery();
		String ret = results.getString(1);
		return ret;
	}

	public String getIdFromEmail(String email) throws SQLException {
		String query = "SELECT user_id FROM users WHERE email_address=?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, email);
		ResultSet results = stat.executeQuery();
		String ret = results.getString(1);
		return ret;
	}

	public String getIdFromUserName(String user) throws SQLException {
		String query = "SELECT user_id FROM users WHERE username=?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, user);
		ResultSet results = stat.executeQuery();
		String ret = results.getString(1);
		return ret;
	}

	public String getPasswordFromUserName(String user) throws SQLException {
		String query = "SELECT password FROM users WHERE username=?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, user);
		ResultSet results = stat.executeQuery();
		String ret = results.getString(1);
		return ret;
	}

	public String certifyLogin(String userName, String pw) throws SQLException {
		System.out.println(userName);
		System.out.println(pw);

		String password = "";
		String id = "";
		if (userName.contains("@")) {
			password = getPasswordFromEmail(userName);
			id = getIdFromEmail(userName);
		} else {
			password = getPasswordFromUserName(userName);
			id = getIdFromUserName(userName);
		}

		if (password.equals(pw)) {
			return id;
		}
		return "";
	}

	/**
	 * function that closes the connection.
	 *
	 * @throws SQLException
	 *             SQL error.
	 */
	public void closeConn() throws SQLException {
		conn.close();
	}

}
