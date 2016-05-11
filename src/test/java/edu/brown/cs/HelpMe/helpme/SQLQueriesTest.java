package edu.brown.cs.helpme;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs.HelpMe.main.SQLQueries;

public class SQLQueriesTest {

  private static SQLQueries sql;

  @BeforeClass
  public static void beforeClass() throws SQLException, ClassNotFoundException {
    String db = "smallDb.db";
    sql = new SQLQueries(db);
  }

  @Test
  public void testConstruction(){
    assertTrue(sql != null);
  }

  @Test
  public void testGetIDFromEmail() throws SQLException{
    String email = "jared_lee@brown.edu";
    String ret = sql.getIdFromEmail(email);
    assertTrue(!ret.equals(""));
  }

  @Test
  public void testGetIDFromUser() throws SQLException{
    String user = "jaredlee";
    String ret = sql.getIdFromUserName(user);
    assertTrue(!ret.equals(""));
  }

  @Test
  public void testGetPWFromEmail() throws SQLException{
    String email = "jared_lee@brown.edu";
    String ret = sql.getPasswordFromEmail(email);
    assertTrue(!ret.equals(""));
  }

  @Test
  public void testGetPWFromUserName() throws SQLException{
    String user = "jaredlee";
    String ret = sql.getPasswordFromUserName(user);
    assertTrue(!ret.equals(""));
  }

  @Test
  public void testGetAllQID() throws SQLException{
    List testList = sql.getAllQIDs();
    assertTrue(testList.size() > 0);
  }

  @Test
  public void testLeaderboard() throws SQLException{
    List testList = sql.getOrderedLeaderboard();
    assertTrue(testList.size() > 0);
  }

  @Test
  public void testLogin() throws SQLException{
    List testList = sql.getOrderedLeaderboard();
    String ret = sql.certifyLogin("jaredlee", "test");
    assertTrue(!ret.equals(""));
  }



}