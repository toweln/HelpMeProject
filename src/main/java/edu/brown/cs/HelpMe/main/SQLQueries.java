package helpMe;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * SQLQueries class. Handles calling queries
 * which are used in other areas of helpMe.
 * @author jplee
 */
public class SQLQueries {
  /**
   * The connection.
   */
  private Connection conn;
  /**
   * SQLQueries constructor that handles setting up the connection to the
   * database. Gets data from the db which is used by the other classes.
   * @param db Path to database file.
   * @throws ClassNotFoundException invalid db file.
   * @throws SQLException SQL error.
   */
  public SQLQueries(String db) throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");
    conn = DriverManager.getConnection("jdbc:sqlite:" + db);
    conn.setAutoCommit(false);
    conn.createStatement();
  }


  public void insertNewUser(String userid, String first, String last,
      String email, String phoneNumber, String pw) throws SQLException {
    String query = "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?)";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, userid);
    stat.setString(2, first);
    stat.setString(3, last);
    stat.setString(4, email);
    stat.setString(5, phoneNumber);
    stat.setString(6, pw);
    stat.executeQuery();
  }

  public void insertNewRequest(String reqid, String tuteeid, String tutorid,
      String tags, String summary, String body, String postLat, String postLon,
      String timePosted, String timeResponded, String rating)
      throws SQLException {
    String query = "INSERT INTO requests VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, reqid);
    stat.setString(2, tuteeid);
    stat.setString(3, tutorid);
    stat.setString(4, tags);
    stat.setString(5, summary);
    stat.setString(6, body);
    stat.setString(7, postLat);
    stat.setString(8, postLon);
    stat.setString(9, timePosted);
    stat.setString(10, timeResponded);
    stat.setString(11, rating);
    stat.executeQuery();
  }

  public void updateRequestBody(String reqid, String body) throws SQLException {
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

  public void updateRating(String rating, String reqid)
      throws SQLException {
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
  /**
   * Gets all actors from the database.
   * @return List of all actor names.
   * @throws SQLException SQL error.
   */
  public List<String> getAllActorsQuery() throws SQLException {
    String query = "SELECT DISTINCT name FROM actor";
    PreparedStatement stat = conn.prepareStatement(query);
    ResultSet results = stat.executeQuery();
    List<String> toReturn = new ArrayList<String>();
    while (results.next()) {
      toReturn.add(results.getString(1));
    }
    stat.close();
    results.close();
    return toReturn;
  }
  /**
   * Given an actor's name, get their ID.
   * @param name The name of an actor.
   * @return The actor's ID.
   * @throws SQLException SQL error.
   */
  public String getActorIDFromNameQuery(String name) throws SQLException {
    String query = "SELECT id FROM actor WHERE name = ? LIMIT 1";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, name);
    ResultSet results = stat.executeQuery();
    //only add if results isn't empty
    String toReturn = "";
    if (results.next()) {
      toReturn = results.getString(1);
    }
    stat.close();
    results.close();
    return toReturn;
  }
  /**
   * Given an actor's ID, get their name.
   * @param id The name of an actor.
   * @return The actor's ID.
   * @throws SQLException SQL error.
   */
  public String getActorNameFromIDQuery(String id) throws SQLException {
    String query = "SELECT name FROM actor WHERE id = ? LIMIT 1";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, id);
    ResultSet results = stat.executeQuery();
    //only add if results isn't empty
    String toReturn = "";
    if (results.next()) {
      toReturn = results.getString(1);
    }
    stat.close();
    results.close();
    return toReturn;
  }
  /**
   * Query that given an film's id, returns the film's name.
   * @param id The id of an actor.
   * @return The actor's ID.
   * @throws SQLException SQL error.
   */
  public String getFilmQuery(String id) throws SQLException {
    String query = "SELECT name FROM film WHERE id = ? LIMIT 1";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, id);
    ResultSet results = stat.executeQuery();
    //only add if results isn't empty
    String toReturn = "";
    if (results.next()) {
      toReturn = results.getString(1);
    }
    stat.close();
    results.close();
    return toReturn;
  }

  /**
   * Given a film's name, return the id.
   * @param name The id of an actor.
   * @return The actor's ID.
   * @throws SQLException SQL error.
   */
  public String getFilmIDQuery(String name) throws SQLException {
    String query = "SELECT id FROM film WHERE name = ? LIMIT 1";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, name);
    ResultSet results = stat.executeQuery();
    //only add if results isn't empty
    String toReturn = "";
    if (results.next()) {
      toReturn = results.getString(1);
    }
    stat.close();
    results.close();
    return toReturn;
  }

  /**
   * Given an aactorID, returns the filmID's of the films the actor has been in.
   * @param actorID actor's ID
   * @return A List of filmIDs that the actor has been in.
   * @throws SQLException SQL error
   */
  public List<String> getActorFilmsIDsQuery(String actorID) throws
  SQLException {
    String query = "SELECT id FROM "
        + "film JOIN actor_film ON "
        + "film.id = actor_film.film "
        + "WHERE actor_film.actor = ?";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, actorID);
    ResultSet results = stat.executeQuery();
    List<String> toReturn = new ArrayList<String>();
    while (results.next()) {
      toReturn.add(results.getString(1));
    }
    stat.close();
    results.close();
    return toReturn;
  }
  /**
   * Given a film's ID, return the list of actor id in the film.
   * @param filmID A film's ID.
   * @return A List of actors that are in a film.
   * @throws SQLException SQL error.
   */
  public List<String> getFilmActorsIDsQuery(String filmID) throws SQLException {
    String query = "SELECT id FROM actor JOIN actor_film ON "
        + "actor.id = actor_film.actor "
        + "WHERE actor_film.film = ?";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, filmID);
    ResultSet results = stat.executeQuery();
    List<String> toReturn = new ArrayList<String>();
    while (results.next()) {
      toReturn.add(results.getString(1));
    }
    return toReturn;
  }

  /**
   * Given a film id, and a letter to match to, return the actor ID from the film
   * who fulfill the caveat.
   * @param firstChar A character for caveat.
   * @param filmID A film's ID.
   * @return A List of actors that are in a film that fulfill the caveat.
   * @throws SQLException SQL error
   */
  public List<String> getFilmActorsCaveatQuery(String firstChar,
      String filmID) throws SQLException {
    String query = "SELECT id FROM actor JOIN actor_film ON "
        + "actor.id = actor_film.actor "
        + "WHERE name LIKE ? AND actor_film.film = ?";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, firstChar + "%");
    stat.setString(2, filmID);
    ResultSet results = stat.executeQuery();
    List<String> toReturn = new ArrayList<String>();
    while (results.next()) {
      toReturn.add(results.getString(1));
    }
    return toReturn;
  }
  /**
   * Given an actorID and actorName, insert into the actor table
   * @param actorID actor id
   * @param actorName actor name
   * @throws SQLException
   */
  public void addActorQuery(String actorID, String actorName)
      throws SQLException {
    String query = "INSERT INTO actor VALUES (?, ?)";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, actorID);
    stat.setString(2, actorName);
    stat.executeQuery();

    String query2 = "INSERT INTO actor_film VALUES (?)";
    PreparedStatement stat2 = conn.prepareStatement(query2);
    stat2.setString(1, actorID);
    stat2.executeQuery();
  }

  /**
   * Given an actorID and actorName, insert into the actor table
   * @param actorID actor id
   * @param actorName actor name
   * @throws SQLException
   */
  public void addFilmQuery(String filmID, String filmName)
      throws SQLException {
    String query = "INSERT INTO film VALUES (?, ?)";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, filmID);
    stat.setString(2, filmName);
    stat.executeQuery();

    String query2 = "INSERT INTO actor_film VALUES (?)";
    PreparedStatement stat2 = conn.prepareStatement(query2);
    stat2.setString(1, filmID);
    stat2.executeQuery();
  }
  /**
   * Given a movie and a film, remove the link between the two.
   * @param movieName The name of the movie
   * @param actorName The name of the actor
   * @throws SQLException
   */
  public void removeActorFromFilmQuery(String movieName, String actorName)
      throws SQLException {
    String query = "DELETE FROM actor_film WHERE film=? AND actor=?";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, getFilmIDQuery(movieName));
    stat.setString(2, getActorIDFromNameQuery(actorName));
    stat.executeQuery();
  }
  /**
   * Given an actor and a film, insert a link between the two.
   * @param actorName The name of the actor
   * @param filmName The name of the movie
   * @throws SQLException
   */
  public void insertActorIntoFilmQuery(String actorName, String filmName)
      throws SQLException {
    String query = "INSERT INTO actor_film VALUES(?, ?)";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, getActorIDFromNameQuery(actorName));
    stat.setString(2, getFilmIDQuery(filmName));
    stat.executeQuery();
  }
  /**
   * query that counts the number of actors starring in a given film (used to
   * calculate weight of a movie edge).
   * @param filmID ID of a film.
   * @return toReturn int count of the number of actors in the film.
   * @throws SQLException SQL error.
   */
  public double queryGetFilmCount(String filmID) throws SQLException {
    String query = "SELECT COUNT(actor) AS cnt FROM actor_film "
        + "WHERE actor_film.film = ?";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, filmID);
    ResultSet results = stat.executeQuery();
    //only add if results isn't empty
    double toReturn = 0;
    if (results.next()) {
      toReturn = results.getDouble(1);
    }
    results.close();
    return toReturn;
  }



  /**
   * function that closes the connection.
   * @throws SQLException SQL error.
   */
  public void closeConn() throws SQLException {
    conn.close();
  }
}
