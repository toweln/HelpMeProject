package edu.brown.cs.HelpMe.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.HelpMe.autocorrect.CommandParser;
import edu.brown.cs.HelpMe.autocorrect.SuggestionGenerator;
import edu.brown.cs.HelpMe.email.EmailSending;
import edu.brown.cs.HelpMe.email.UserData;
import edu.brown.cs.HelpMe.chat.Chat;
import freemarker.template.Configuration;

public class Main {
  public static void main(String[] args) throws SQLException,
      UnknownHostException {
    new Main(args).run();
  }

  private String[] args;
  private static SQLQueries dbQuery;
  private static final Gson GSON = new Gson();
  private CommandParser cp;
  private static SuggestionGenerator sg;
  private User currentUser;
  private static EmailSending emailSender;
  private static String userID;

  private Main(String[] args) {
    this.args = args;
  }
  /**
   * Starts the server.
   * @throws SQLException
   * @throws UnknownHostException
   */
  private void run() throws SQLException, UnknownHostException {
    userID = "";
    emailSender = new EmailSending();
    OptionParser parser = new OptionParser();
    String database = "smallDb.db";
    try {
      dbQuery = new SQLQueries(database);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    parser.accepts("gui");
    OptionSet options = parser.parse(args);

    this.cp = new CommandParser(false, 2, true, false, false);
    this.sg = null;
    try {
      TagDatabase td = new TagDatabase();
      this.sg = new SuggestionGenerator(td, cp);
    } catch (FileNotFoundException e) {
      System.out.println("ERROR: Please specify a text file.");
      System.exit(1);
    } catch (SQLException e) {
      System.out.println("ERROR: Database does not exist");
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: Cannot find table");
    }

    TagDatabase td = new TagDatabase();
    TutorCompatibility tc = new TutorCompatibility(td);
    List<Question> sortedQuestions = new ArrayList<>();
    try {
      sortedQuestions = tc
          .getSortedQuestions("2317b198-561d-4b05-8a7c-5bbd2b0df4e3");
    } catch (SQLException e) {
      System.out.println("ERROR: Database does not exist");
    }
    Chat c = new Chat();
    c.initializeSocket();
    runSparkServer();
    c.startSocket();
  }

  /**
   * Creates the freemarkerengine for spark.
   * @return
   */
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.\n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * Runs the spark server.
   */
  private void runSparkServer() {
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/index.html", new FrontHandler(), freeMarker);
    Spark.get("/home.html", new HomeHandler(), freeMarker);
    Spark.get("/leaderboard.html", new LeaderboardHandler(), freeMarker);
    // SPARK REQUESTS I WROTE --JARED

    // Post request for signup
    Spark.post("/newUser", new signupHandler());
    Spark.post("/closeQuestion", new closeQuestionhandler());

    Spark.post("/login", new LoginHandler());
    Spark.post("/suggest", new SuggestHandler());
    Spark.get("/signup.html", new SignupDropdownHandler(), freeMarker);
    Spark.get("/q_new.html", new NewQuestionHandler(), freeMarker);
    Spark.get("/q.html", new SubmittedQuestion(), freeMarker);
    Spark.get("/settings.html", new SettingsHandler(), freeMarker);
    Spark.post("/sortedQs", new SortedQuestionHandler());
    Spark.post("/insertQ", new InsertQuestionHandler());
    Spark.get("/questions/:questionID", new QuestionPageHandler(), freeMarker);
    Spark.get("/profiles/:userID", new ProfileHandler(), freeMarker);
    Spark.get("/room/:roomID", new ChatroomHandler(), freeMarker);
    Spark.get("/rating/:tutorID", new RatingHandler(), freeMarker);
    Spark.post("/insertRating", new InsertRatingHandler());
  }

  /**
   * Handler for the homepage frontend.
   * @author idk
   */
  private class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
      return new ModelAndView(variables, "index.html");
    }
  }

  /**
   * Handler for the chatroom.
   * @author
   */
  private static class ChatroomHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String roomID = req.params(":roomID");

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .build();
      return new ModelAndView(variables, "chatroom.html");
    }
  }

  /**
   * Frontend handler for the home page.
   * @author
   */
  private class HomeHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      // Sends information to map
      List<String> qs = new ArrayList<>();
      try {
        qs = dbQuery.getAllQIDs();
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("ERROR: Database does not exist");
      }
      Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
      return new ModelAndView(variables, "home.html");
    }
  }

  /**
   * Handler for displaying the leaderboard.
   * @author
   */
  private class LeaderboardHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      List<LeaderboardEntry> leaders = new ArrayList<>();
      try {
        leaders = dbQuery.getOrderedLeaderboard();
      } catch (SQLException e) {
        System.out.println("ERROR: Database does not exist");
      }

      String username1 = leaders.get(0).getUsername();
      String rating1 = leaders.get(0).getRating();
      String numAns1 = leaders.get(0).getNumQuestionsAnswered();
      String username2 = leaders.get(1).getUsername();
      String rating2 = leaders.get(1).getRating();
      String numAns2 = leaders.get(1).getNumQuestionsAnswered();
      String username3 = leaders.get(2).getUsername();
      String rating3 = leaders.get(2).getRating();
      String numAns3 = leaders.get(2).getNumQuestionsAnswered();
      String username4 = leaders.get(3).getUsername();
      String rating4 = leaders.get(3).getRating();
      String numAns4 = leaders.get(3).getNumQuestionsAnswered();
      String username5 = leaders.get(4).getUsername();
      String rating5 = leaders.get(4).getRating();
      String numAns5 = leaders.get(4).getNumQuestionsAnswered();

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "HelpMe!").put("username1", username1)
          .put("rating1", rating1).put("numAns1", numAns1)
          .put("username2", username2).put("rating2", rating2)
          .put("numAns2", numAns2).put("username3", username3)
          .put("rating3", rating3).put("numAns3", numAns3)
          .put("username4", username4).put("rating4", rating4)
          .put("numAns4", numAns4).put("username5", username5)
          .put("rating5", rating5).put("numAns5", numAns5).build();
      return new ModelAndView(variables, "leaderboard.html");
    }
  }

  /**
   * Handler for the logged in user's profile info.
   * @author
   */
  private class ProfileHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String userID = req.params(":userID");
      User u = null;
      try {
        u = dbQuery.makeUserProfile(userID);
      } catch (SQLException e) {
        System.out.println("ERROR: Database does not exist");
      }

      String name = u.getName();
      String username = u.getUsername();
      String email = u.getEmail();
      String tags = u.getFrontEndTags();

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("name", name).put("email", email).put("username", username)
          .put("tags", tags).build();
      return new ModelAndView(variables, "profile.html");
    }
  }

  /**
   * Handler for the page where a tutor is rated.
   * @author
   */
  private class RatingHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String userID = req.params(":tutor");

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .build();
      return new ModelAndView(variables, "rating.html");
    }
  }

  /**
   * Handler for the settings page which doesn't exist I think.
   * @author
   */
  private class SettingsHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
      return new ModelAndView(variables, "settings.html");
    }
  }

  /**
   * Handler for the drop down part which happens on signup.
   * @author
   */
  private class SignupDropdownHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
      return new ModelAndView(variables, "signup.html");
    }
  }

  /**
   * Handler for taking you to a question.
   * @author
   */
  private static class QuestionPageHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String qID = req.params(":questionID");
      Question q = null;
      try {
        q = dbQuery.makeQuestion(qID);
      } catch (SQLException e) {
        System.out.println("ERROR: Database does not exist");
      }

      String questionTitle = q.getTitle();
      String questionMessage = q.getMessage();
      questionTitle = questionTitle.replaceAll("\"", "");
      questionMessage = questionMessage.replaceAll("\"", "");

      Map<String, String> variables = ImmutableMap.of("title", "HelpMe!",
          "questionTitle", questionTitle, "questionMessage", questionMessage);
      return new ModelAndView(variables, "q.html");
    }
  }

  /**
   * Handler for creating a new question.
   * @author
   *
   */
  private class NewQuestionHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
      return new ModelAndView(variables, "q_new.html");
    }
  }

  /**
   * Handler for a submitted question.
   * @author
   */
  private class SubmittedQuestion implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String questionTitle = qm.value("title");
      String questionMessage = qm.value("message");

      Map<String, String> variables = ImmutableMap.of("title", "HelpMe!",
          "questionTitle", questionTitle, "questionMessage", questionMessage);
      return new ModelAndView(variables, "q.html");
    }
  }

  /**
   * Handler to login in users. Checks if the supplied information corresponds
   * to an actual user. Will return the userid if the user login was successful.
   * @author Jared
   */
  private static class LoginHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String userName = qm.value("username");
      String password = qm.value("password");
      userName = userName.toLowerCase();
      userName = userName.substring(1, userName.length() - 1);
      password = password.substring(1, password.length() - 1);
      String status = "";
      Boolean ret = true;
      try {
        status = dbQuery.certifyLogin(userName, password);
      } catch (SQLException e) {
        e.printStackTrace();
      }
      if (status.equals("")) {
        ret = false;
      } else {
        userID = status;
      }
      return GSON.toJson(status);
    }
  }

  /**
   * Handler for handling responding to a question and closing it.
   * @author Jared
   */
  private static class closeQuestionhandler implements Route {
    @Override
    public Object handle(Request req, Response res) throws UnknownHostException {
      QueryParamsMap qm = req.queryMap();
      String request = qm.value("reqid");
      String tutor = qm.value("userid");
      request = request.substring(1, request.length() - 1);
      tutor = tutor.substring(1, tutor.length() - 1);


      Boolean status = false;
      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      Date date = new Date();
      String dateString = dateFormat.format(date);

      InetAddress lh = InetAddress.getLocalHost();
      String hostAddress = lh.getHostAddress();

      try {
        dbQuery.updateRequestTutor(request, tutor);
        dbQuery.updateTimeResponded(dateString, request);
        dbQuery.updateQuestionsAnswered(tutor);

        String tuteeId = dbQuery.getTuteeFromReqId(request);
        UserData tuteeUser = dbQuery.getUserDataFromId(tuteeId);
        UserData tutorUser = dbQuery.getUserDataFromId(tutor);
        String summary = dbQuery.getRequestSummary(request);
        String chatRoomURL = hostAddress + ":4567/room/" + request;
        String ratingURL = hostAddress + ":4567/rating/" + request;

        emailSender.sendTutorEmail(tutorUser.getEmail(), summary,
            tuteeUser.getFirstName(), chatRoomURL);
        emailSender.sendTuteeEmail(tuteeUser.getEmail(), summary,
            tutorUser.getFirstName(), chatRoomURL, ratingURL);
      } catch (SQLException | MessagingException e) {
        e.printStackTrace();
      }

      return GSON.toJson(status);
    }
  }

  /**
   * Handler for inserting a new question into the db.
   * @author Jared
   */
  private static class InsertQuestionHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      String user = qm.value("userid");
      user = user.substring(1, user.length() - 1);

      String title = qm.value("title");
      String body = qm.value("message");
      body = body.replaceAll("\\\\n", "<br>");
      String topics = qm.value("topics");
      // ADDED LATITUDE AND LONGITUDE
      String lat = qm.value("lat");
      String lon = qm.value("lng");
      List<String> topicsList = Arrays.asList(topics.substring(1,
          topics.length() - 1).split("\\s*,\\s*"));
      String reqid = UUID.randomUUID().toString();
      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      Date date = new Date();
      String dateString = dateFormat.format(date);
      try {
        dbQuery.insertNewRequest(reqid, user, "", "", topicsList, title, body,
            lat, lon, dateString, "", "");
        dbQuery.updateQuestionsAsked(user);
        dbQuery.updateWordCount(topicsList, body);
      } catch (SQLException e) {
        e.printStackTrace();
      }
      boolean ret = true;

      return GSON.toJson(ret);
    }
  }

  /**
   * Handler for rating a user in the db.
   * @author Jared
   */
  private static class InsertRatingHandler implements Route {
    @Override
    public Object handle(Request req, Response res) throws SQLException {
      QueryParamsMap qm = req.queryMap();
      String rating = qm.value("rate");
      String reqid = qm.value("reqid");
      rating = rating.substring(1, rating.length() - 1);
      reqid = reqid.substring(1, reqid.length() - 1);
      dbQuery.insertRating(rating, reqid);
      return GSON.toJson(null);
    }
  }

  /**
   * Handler for returning sorted questions to the frontend.
   * @author
   */
  private static class SortedQuestionHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String user = qm.value("userid");
      user = user.substring(1, user.length() - 1);
      TagDatabase td = new TagDatabase();
      TutorCompatibility tc = new TutorCompatibility(td);
      List<Question> sortedQuestions = new ArrayList<>();
      try {
        sortedQuestions = tc.getSortedQuestions(user);
      } catch (SQLException e) {
        System.out.println("ERROR: Database does not exist");
      }

      return GSON.toJson(sortedQuestions);
    }
  }

  /**
   * Handler forhandling signups.Returns true if signup was
   * successful,false*otherwise. This will be changed to return the userid
   * string of the user that was just created.
   * @author Jared
   */
  private static class signupHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String userName = qm.value("username");
      String password = qm.value("password");
      String first = qm.value("first_name");
      String last = qm.value("last_name");
      String email = qm.value("email");
      String phone = qm.value("phone_number");
      String topics = qm.value("topics");
      userName = userName.toLowerCase();
      email = email.toLowerCase();
      List<String> topicsList = Arrays.asList(topics.substring(1,
          topics.length() - 1).split("\\s*,\\s*"));

      userName = userName.substring(1, userName.length() - 1);
      password = password.substring(1, password.length() - 1);
      first = first.substring(1, first.length() - 1);
      last = last.substring(1, last.length() - 1);
      email = email.substring(1, email.length() - 1);
      phone = phone.substring(1, phone.length() - 1);
      topics = topics.substring(1, topics.length() - 1);

      UUID newID = UUID.randomUUID();
      Boolean status = false;
      userID = "";
      try {
        status = dbQuery.insertNewUser(newID.toString(), first, last, email,
            phone, userName, password);
        dbQuery.insertUserExpertise(topicsList, newID.toString());
        if (status) {
          userID = newID.toString();
          emailSender.sendWelcomeEmail(email);
        } else {
          userID = "";
        }
      } catch (SQLException | MessagingException e) {
        e.printStackTrace();
      }
      return GSON.toJson(userID);
    }
  }

  /**
   * Handler for suggestions of something.
   * @author
   */
  private static class SuggestHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String userInput = qm.value("input");
      List<String> suggestions = new ArrayList<>();
      if (userInput.length() == 0) {
        return suggestions;
      }
      suggestions = sg.getSuggestions(userInput);
      List<String> capSuggs = new ArrayList<>();
      for (String sugg : suggestions) {
        StringBuffer sb = new StringBuffer();

        String[] split = sugg.split(" ");
        for (String str : split) {
          char[] stringArray = str.trim().toCharArray();
          stringArray[0] = Character.toUpperCase(stringArray[0]);
          str = new String(stringArray);

          sb.append(str).append(" ");
        }
        capSuggs.add(sb.toString());
      }
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("suggestions", capSuggs).build();
      return GSON.toJson(variables);
    }
  }

  /**
   * Handler for checking if a user is logged in. Returns true if a user is
   * logged in, false otherwise.
   * @author Jared
   */
  private static class checkLoginHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      boolean logged = false;
      if (userID.length() >= 1) {
        logged = true;
      }
      return GSON.toJson(logged);
    }
  }

  /**
   * Handler for 500 errors.
   * @author
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}