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
	private Map<String, String> discMap;
	private List<String> allDisc;

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
		conn.createStatement();
		mathDisc = new ArrayList<String>();
		sciDisc = new ArrayList<String>();
		humDisc = new ArrayList<String>();
		csDisc = new ArrayList<String>();
		histDisc = new ArrayList<String>();

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

	public Boolean emailUnique(String email) throws SQLException{
    String query = "SELECT COUNT(*) FROM users WHERE users.email_address = ?";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, email);
    ResultSet results = stat.executeQuery();
    int ret = results.getInt(1);
    System.out.println("ret");
    System.out.println(ret);
    if(ret != 0){
      return false;
    }
    return true;
  }

  public Boolean userUnique(String user) throws SQLException{
    String query = "SELECT COUNT(*) FROM users WHERE users.username = ?";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, user);
    ResultSet results = stat.executeQuery();
    int ret = results.getInt(1);
    System.out.println("ret");
    System.out.println(ret);
    if(ret != 0){
      return false;
    }
    return true;
  }


  public Boolean insertNewUser(String userid, String first, String last,
      String email, String phoneNumber, String userName, String pw) throws SQLException {
    if(!emailUnique(email)){
      return false;
    }
    if(!userUnique(userName)){
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
    for(int i = 0; i < toprint.size(); i++){
      System.out.println(toprint.get(i));
    }
    stat.setString(1, userid);
    stat.setString(2, first);
    stat.setString(3, last);
    stat.setString(4, email);
    stat.setString(5, phoneNumber);
    stat.setString(6, userName);
    stat.setString(7, pw);
   int test =  stat.executeUpdate();
   //conn.commit();
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
			sbTags.append(t.replaceAll("^\"|\"$", ""));
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
		// System.out.println(query);
		PreparedStatement stat = conn.prepareStatement(query);
		int test = stat.executeUpdate();

		// stat.setString(1, tagList);
		// stat.setString(2, ratingsList);
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
		String query = "SELECT body, rating FROM requests WHERE request_id = ?;";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, qID);
		ResultSet rs = stat.executeQuery();
		String body = "";
		String rating = "";
		while (rs.next()) {
			body = rs.getString(1);
			rating = rs.getString(2);
		}
		System.out.println("RATING: " + rating);
		rs.close();
		// List<String> qBodyWords = Arrays.asList(body.split("\\s+"));
		List<String> qTags = Arrays.asList(rating.split("\\s+"));
		Map<String, Map<String, Double>> qoverallRating = new HashMap<>();
		for (String tag : qTags) {
			String currDisc = discMap.get(tag);
			if (qoverallRating.containsKey(currDisc)) {
				qoverallRating.get(currDisc).put(tag, 1.0);
			} else {
				Map<String, Double> newDiscMap = new HashMap<>();
				qoverallRating.put(currDisc, newDiscMap);
				qoverallRating.get(currDisc).put(tag, 1.0);
			}
		}
		System.out.println(qoverallRating);
		TagDatabase td = new TagDatabase();
		TagRating trq = new TagRating(qoverallRating, td);
		Question q = new Question("", body, trq, td);
		return q;
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
		System.out.println(ratingMap);
		TagDatabase td = new TagDatabase();
		TagRating tru = new TagRating(ratingMap, td);
		User u = new User("", tru);
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
		//Still need to figure out what's going on with tags and stuff
		//String nTags = this.tagsToString(disc, tags);
		String nTags = "";
		stat.setString(5, nTags);
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
		String query = "UPDATE requests SET tutor_id=? WHERE request_id=?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, tutor);
		stat.setString(2, reqid);
		stat.executeQuery();
	}

	public void updateTimeResponded(String timeRes, String reqid)
			throws SQLException {
		String query = "UPDATE requests SET time_responded=? WHERE request_id=?";
		PreparedStatement stat = conn.prepareStatement(query);
		stat.setString(1, timeRes);
		stat.setString(2, reqid);
		stat.executeQuery();
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
    System.out.println(pw);
    System.out.println(password);
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

	/*  *//**
			 * Gets all actors from the database.
			 *
			 * @return List of all actor names.
			 * @throws SQLException
			 *             SQL error.
			 */
	/*
	 * public List<String> getAllActorsQuery() throws SQLException { String
	 * query = "SELECT DISTINCT name FROM actor"; PreparedStatement stat =
	 * conn.prepareStatement(query); ResultSet results = stat.executeQuery();
	 * List<String> toReturn = new ArrayList<String>(); while (results.next()) {
	 * toReturn.add(results.getString(1)); } stat.close(); results.close();
	 * return toReturn; }
	 *//**
		 * Given an actor's name, get their ID.
		 *
		 * @param name
		 *            The name of an actor.
		 * @return The actor's ID.
		 * @throws SQLException
		 *             SQL error.
		 */
	/*
	 * public String getActorIDFromNameQuery(String name) throws SQLException {
	 * String query = "SELECT id FROM actor WHERE name = ? LIMIT 1";
	 * PreparedStatement stat = conn.prepareStatement(query); stat.setString(1,
	 * name); ResultSet results = stat.executeQuery(); //only add if results
	 * isn't empty String toReturn = ""; if (results.next()) { toReturn =
	 * results.getString(1); } stat.close(); results.close(); return toReturn; }
	 *//**
		 * Given an actor's ID, get their name.
		 *
		 * @param id
		 *            The name of an actor.
		 * @return The actor's ID.
		 * @throws SQLException
		 *             SQL error.
		 */
	/*
	 * public String getActorNameFromIDQuery(String id) throws SQLException {
	 * String query = "SELECT name FROM actor WHERE id = ? LIMIT 1";
	 * PreparedStatement stat = conn.prepareStatement(query); stat.setString(1,
	 * id); ResultSet results = stat.executeQuery(); //only add if results isn't
	 * empty String toReturn = ""; if (results.next()) { toReturn =
	 * results.getString(1); } stat.close(); results.close(); return toReturn; }
	 *//**
		 * Query that given an film's id, returns the film's name.
		 *
		 * @param id
		 *            The id of an actor.
		 * @return The actor's ID.
		 * @throws SQLException
		 *             SQL error.
		 */
	/*
	 * public String getFilmQuery(String id) throws SQLException { String query
	 * = "SELECT name FROM film WHERE id = ? LIMIT 1"; PreparedStatement stat =
	 * conn.prepareStatement(query); stat.setString(1, id); ResultSet results =
	 * stat.executeQuery(); //only add if results isn't empty String toReturn =
	 * ""; if (results.next()) { toReturn = results.getString(1); }
	 * stat.close(); results.close(); return toReturn; }
	 *//**
		 * Given a film's name, return the id.
		 *
		 * @param name
		 *            The id of an actor.
		 * @return The actor's ID.
		 * @throws SQLException
		 *             SQL error.
		 */
	/*
	 * public String getFilmIDQuery(String name) throws SQLException { String
	 * query = "SELECT id FROM film WHERE name = ? LIMIT 1"; PreparedStatement
	 * stat = conn.prepareStatement(query); stat.setString(1, name); ResultSet
	 * results = stat.executeQuery(); //only add if results isn't empty String
	 * toReturn = ""; if (results.next()) { toReturn = results.getString(1); }
	 * stat.close(); results.close(); return toReturn; }
	 *//**
		 * Given an aactorID, returns the filmID's of the films the actor has
		 * been in.
		 *
		 * @param actorID
		 *            actor's ID
		 * @return A List of filmIDs that the actor has been in.
		 * @throws SQLException
		 *             SQL error
		 */
	/*
	 * public List<String> getActorFilmsIDsQuery(String actorID) throws
	 * SQLException { String query = "SELECT id FROM " +
	 * "film JOIN actor_film ON " + "film.id = actor_film.film " +
	 * "WHERE actor_film.actor = ?"; PreparedStatement stat =
	 * conn.prepareStatement(query); stat.setString(1, actorID); ResultSet
	 * results = stat.executeQuery(); List<String> toReturn = new
	 * ArrayList<String>(); while (results.next()) {
	 * toReturn.add(results.getString(1)); } stat.close(); results.close();
	 * return toReturn; }
	 *//**
		 * Given a film's ID, return the list of actor id in the film.
		 *
		 * @param filmID
		 *            A film's ID.
		 * @return A List of actors that are in a film.
		 * @throws SQLException
		 *             SQL error.
		 */
	/*
	 * public List<String> getFilmActorsIDsQuery(String filmID) throws
	 * SQLException { String query = "SELECT id FROM actor JOIN actor_film ON "
	 * + "actor.id = actor_film.actor " + "WHERE actor_film.film = ?";
	 * PreparedStatement stat = conn.prepareStatement(query); stat.setString(1,
	 * filmID); ResultSet results = stat.executeQuery(); List<String> toReturn =
	 * new ArrayList<String>(); while (results.next()) {
	 * toReturn.add(results.getString(1)); } return toReturn; }
	 *//**
		 * Given a film id, and a letter to match to, return the actor ID from
		 * the film who fulfill the caveat.
		 *
		 * @param firstChar
		 *            A character for caveat.
		 * @param filmID
		 *            A film's ID.
		 * @return A List of actors that are in a film that fulfill the caveat.
		 * @throws SQLException
		 *             SQL error
		 */
	/*
	 * public List<String> getFilmActorsCaveatQuery(String firstChar, String
	 * filmID) throws SQLException { String query =
	 * "SELECT id FROM actor JOIN actor_film ON " +
	 * "actor.id = actor_film.actor " +
	 * "WHERE name LIKE ? AND actor_film.film = ?"; PreparedStatement stat =
	 * conn.prepareStatement(query); stat.setString(1, firstChar + "%");
	 * stat.setString(2, filmID); ResultSet results = stat.executeQuery();
	 * List<String> toReturn = new ArrayList<String>(); while (results.next()) {
	 * toReturn.add(results.getString(1)); } return toReturn; }
	 *//**
		 * Given an actorID and actorName, insert into the actor table
		 *
		 * @param actorID
		 *            actor id
		 * @param actorName
		 *            actor name
		 * @throws SQLException
		 */
	/*
	 * public void addActorQuery(String actorID, String actorName) throws
	 * SQLException { String query = "INSERT INTO actor VALUES (?, ?)";
	 * PreparedStatement stat = conn.prepareStatement(query); stat.setString(1,
	 * actorID); stat.setString(2, actorName); stat.executeQuery();
	 *
	 * String query2 = "INSERT INTO actor_film VALUES (?)"; PreparedStatement
	 * stat2 = conn.prepareStatement(query2); stat2.setString(1, actorID);
	 * stat2.executeQuery(); }
	 *//**
		 * Given an actorID and actorName, insert into the actor table
		 *
		 * @param actorID
		 *            actor id
		 * @param actorName
		 *            actor name
		 * @throws SQLException
		 */
	/*
	 * public void addFilmQuery(String filmID, String filmName) throws
	 * SQLException { String query = "INSERT INTO film VALUES (?, ?)";
	 * PreparedStatement stat = conn.prepareStatement(query); stat.setString(1,
	 * filmID); stat.setString(2, filmName); stat.executeQuery();
	 *
	 * String query2 = "INSERT INTO actor_film VALUES (?)"; PreparedStatement
	 * stat2 = conn.prepareStatement(query2); stat2.setString(1, filmID);
	 * stat2.executeQuery(); }
	 *//**
		 * Given a movie and a film, remove the link between the two.
		 *
		 * @param movieName
		 *            The name of the movie
		 * @param actorName
		 *            The name of the actor
		 * @throws SQLException
		 */
	/*
	 * public void removeActorFromFilmQuery(String movieName, String actorName)
	 * throws SQLException { String query =
	 * "DELETE FROM actor_film WHERE film=? AND actor=?"; PreparedStatement stat
	 * = conn.prepareStatement(query); stat.setString(1,
	 * getFilmIDQuery(movieName)); stat.setString(2,
	 * getActorIDFromNameQuery(actorName)); stat.executeQuery(); }
	 *//**
		 * Given an actor and a film, insert a link between the two.
		 *
		 * @param actorName
		 *            The name of the actor
		 * @param filmName
		 *            The name of the movie
		 * @throws SQLException
		 */
	/*
	 * public void insertActorIntoFilmQuery(String actorName, String filmName)
	 * throws SQLException { String query =
	 * "INSERT INTO actor_film VALUES(?, ?)"; PreparedStatement stat =
	 * conn.prepareStatement(query); stat.setString(1,
	 * getActorIDFromNameQuery(actorName)); stat.setString(2,
	 * getFilmIDQuery(filmName)); stat.executeQuery(); }
	 *//**
		 * query that counts the number of actors starring in a given film (used
		 * to calculate weight of a movie edge).
		 *
		 * @param filmID
		 *            ID of a film.
		 * @return toReturn int count of the number of actors in the film.
		 * @throws SQLException
		 *             SQL error.
		 */
	/*
	 * public double queryGetFilmCount(String filmID) throws SQLException {
	 * String query = "SELECT COUNT(actor) AS cnt FROM actor_film " +
	 * "WHERE actor_film.film = ?"; PreparedStatement stat =
	 * conn.prepareStatement(query); stat.setString(1, filmID); ResultSet
	 * results = stat.executeQuery(); //only add if results isn't empty double
	 * toReturn = 0; if (results.next()) { toReturn = results.getDouble(1); }
	 * results.close(); return toReturn; }
	 */

}
