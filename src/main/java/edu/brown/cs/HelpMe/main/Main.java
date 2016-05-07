package edu.brown.cs.HelpMe.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import edu.brown.cs.acj.chat.Chat;
import freemarker.template.Configuration;

public class Main {
	public static void main(String[] args) throws SQLException {
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

	private void run() throws SQLException {
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

		// if (options.has("gui")) {
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

		// TagDatabase td = new TagDatabase();
		// TutorCompatibility tc = new TutorCompatibility(td);
		// List<Question> sortedQuestions = new ArrayList<>();
		// try {
		// sortedQuestions = tc
		// .getSortedQuestions("d53438cf-aa97-49d6-a0a0-ff5db0728806");
		// for (Question q : sortedQuestions) {
		// // System.out.println(q.getID());
		// System.out.println(q.getMessage());
		// // System.out.println(q.getRating().getRating());
		// }
		// } catch (SQLException e) {
		// System.out.println("ERROR: Database does not exist");
		// }

		dbQuery.initializeExistingCounts();
		Chat c = new Chat();
		c.initializeSocket();
		runSparkServer();
		c.startSocket();
		// } else {
		// Process commands
		// }
	}

	private static FreeMarkerEngine createEngine() {
		Configuration config = new Configuration();
		File templates = new File(
				"src/main/resources/spark/template/freemarker");
		try {
			config.setDirectoryForTemplateLoading(templates);
		} catch (IOException ioe) {
			System.out.printf("ERROR: Unable use %s for template loading.\n",
					templates);
			System.exit(1);
		}
		return new FreeMarkerEngine(config);
	}

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
		// Spark.get("/profile.html", new ProfileHandler(), freeMarker);
		Spark.get("/settings.html", new SettingsHandler(), freeMarker);
		Spark.post("/sortedQs", new SortedQuestionHandler());
		Spark.post("/insertQ", new InsertQuestionHandler());
		Spark.get("/questions/:questionID", new QuestionPageHandler(),
				freeMarker);
		Spark.get("/profiles/:userID", new ProfileHandler(), freeMarker);
		Spark.get("/room/:roomID", new ChatroomHandler(), freeMarker);
	}

	private class FrontHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response res) {

			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
			return new ModelAndView(variables, "index.html");
		}
	}

	private static class ChatroomHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response res) {
			String roomID = req.params(":roomID");

			Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
					.build();
			return new ModelAndView(variables, "chatroom.html");
		}
	}

	private class HomeHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response res) {
			
			// Sends information to map
			List<String> qs = new ArrayList<>();
			try {
				qs = dbQuery.getAllQIDs();
			} catch (SQLException e) {
				System.out.println("ERROR: Database does not exist");
			}
			
			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
			return new ModelAndView(variables, "home.html");
		}
	}

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

	private class ProfileHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response res) {
			String userID = req.params(":userID");
			// System.out.println("ID: " + userID);
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
					.put("name", name).put("email", email)
					.put("username", username).put("tags", tags).build();
			return new ModelAndView(variables, "profile.html");
		}
	}

	private class SettingsHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response res) {

			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
			return new ModelAndView(variables, "settings.html");
		}
	}

	private class SignupDropdownHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response res) {
			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
			return new ModelAndView(variables, "signup.html");
		}
	}

	private static class QuestionPageHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response res) {
			String qID = req.params(":questionID");
			System.out.println("ID: " + qID);
			Question q = null;
			try {
				q = dbQuery.makeQuestion(qID);
			} catch (SQLException e) {
				System.out.println("ERROR: Database does not exist");
			}

			String questionTitle = q.getTitle();
			String questionMessage = q.getMessage();
			System.out.println(questionTitle);
			System.out.println(questionMessage);
			questionTitle = questionTitle.replaceAll("\"", "");
			questionMessage = questionMessage.replaceAll("\"", "");
			System.out.println(questionTitle);
			System.out.println(questionMessage);

			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!",
					"questionTitle", questionTitle, "questionMessage",
					questionMessage);
			return new ModelAndView(variables, "q.html");
		}
	}

	private class NewQuestionHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response res) {

			// QueryParamsMap qm = req.queryMap();
			// String questionTitle = qm.value("title");
			// String questionMessage = qm.value("message");
			// // String tags = qm.value("tags");
			//
			// // List<String> lTags = Arrays.asList(tags.split("\\s*,\\s*"));
			// System.out.println(questionTitle);
			//
			// String reqid = UUID.randomUUID().toString();
			//
			// try {
			// dbQuery.insertNewRequest(reqid, userID, "", "", null,
			// questionTitle, questionMessage, "", "", "", "", "");
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }

			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
			return new ModelAndView(variables, "q_new.html");
		}
	}

	// private class ProfileHandler implements TemplateViewRoute {
	// @Override
	// public ModelAndView handle(Request req, Response res) {
	// Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
	// return new ModelAndView(variables, "profile.html");
	// }
	// }

	private class SubmittedQuestion implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response res) {
			QueryParamsMap qm = req.queryMap();
			String questionTitle = qm.value("title");
			String questionMessage = qm.value("message");

			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!",
					"questionTitle", questionTitle, "questionMessage",
					questionMessage);
			return new ModelAndView(variables, "q.html");
		}
	}

	/**
	 * Handler to login in users. Checks if the supplied information corresponds
	 * to an actual user. Will return the userid if the user login was
	 * successful.
	 *
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
			System.out.println(userName);
			System.out.println(password);
			String status = "";
			Boolean ret = true;
			try {
				status = dbQuery.certifyLogin(userName, password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (status.equals("")) {
				ret = false;
				System.out.println("false");
			} else {
				userID = status;
			}
			return GSON.toJson(status);

		}
	}

	/**
	 * Class for handling responding to a question and closing it.
	 *
	 * @author Jared
	 */
	private static class closeQuestionhandler implements Route {
		@Override
		public Object handle(Request req, Response res) {
			QueryParamsMap qm = req.queryMap();
			String request = qm.value("reqid");
			System.out.println("REQ ID: " + request);
			String tutor = qm.value("userid");
			request = request.substring(1, request.length() - 1);
			Boolean status = false;
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String dateString = dateFormat.format(date);
			try {
				dbQuery.updateRequestTutor(request, userID);
				dbQuery.updateTimeResponded(dateString, request);
				String tuteeId = dbQuery.getTuteeFromReqId(request);
				UserData tuteeUser = dbQuery.getUserDataFromId(tuteeId);
				UserData tutorUser = dbQuery.getUserDataFromId(tutor);
				String summary = dbQuery.getRequestSummary(request);
				emailSender.sendTutorEmail(tutorUser.getEmail(), summary,
						tuteeUser.getFirstName(), "CHAT LINK GOES HERE");
				emailSender.sendTuteeEmail(tuteeUser.getEmail(), summary,
						tutorUser.getFirstName(), "CHAT LINK GOES HERE");
			} catch (SQLException | MessagingException e) {
				e.printStackTrace();
			}

			return GSON.toJson(status);
		}
	}

	private static class InsertQuestionHandler implements Route {
		@Override
		public Object handle(Request req, Response res) {
			System.out.println("INSERT HANDLER");
			QueryParamsMap qm = req.queryMap();
			// THIS SHOULD BE ADDED WHEN USERID COOKIES ARE IMPLEMENTED ON
			// FRONTEND
			String user = qm.value("userid");
			// THIS SHOULD BE ADDED WHEN USERID COOKIES ARE IMPLEMENTED ON
			// FRONTEND

			String title = qm.value("title");
			String body = qm.value("message");
			String topics = qm.value("topics");
			//ADDED LATITUDE AND LONGITUDE
			String lat = qm.value("lat");
			String lon = qm.value("lng");
			List<String> topicsList = Arrays.asList(topics
					.substring(1, topics.length() - 1).split("\\s*,\\s*"));
			System.out.println("BODY " + body);
			String reqid = UUID.randomUUID().toString();
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String dateString = dateFormat.format(date);
			try {
				dbQuery.insertNewRequest(reqid, user, "", "", topicsList, title,
						body, lat, lon, dateString, "", "");
				dbQuery.updateWordCount(topicsList, body);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			boolean ret = true;
			// System.out.println("false");
			// } else {
			// userID = status;
			// }
			return GSON.toJson(ret);
		}
	}

	private static class SortedQuestionHandler implements Route {
		@Override
		public Object handle(Request req, Response res) {
			QueryParamsMap qm = req.queryMap();
			String user = qm.value("userid");
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
	 *
	 * @author Jared
	 *
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
			List<String> topicsList = Arrays.asList(topics
					.substring(1, topics.length() - 1).split("\\s*,\\s*"));

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
				status = dbQuery.insertNewUser(newID.toString(), first, last,
						email, phone, userName, password);
				dbQuery.insertUserExpertise(topicsList, newID.toString());
				if (status) {
					userID = newID.toString();
					emailSender.sendWelcomeEmail(email);
					System.out.println("new user success");
				} else {
					userID = "";
					System.out.println("user fail");
				}
			} catch (SQLException | MessagingException e) {
				e.printStackTrace();
			}

			return GSON.toJson(userID);
		}
	}

	// private static class SubmitQuestionHandler implements Route {
	// @Override
	// public Object handle(Request req, Response res) {
	// QueryParamsMap qm = req.queryMap();
	// String title = qm.value("title");
	// String rawTags = qm.value("tags");
	// String body = qm.value("message");
	// List<String> tags = Arrays.asList(rawTags.split("\\s*,\\s*"));
	// String message = qm.value("message");
	// String reqid = UUID.randomUUID().toString();
	// try {
	// dbQuery.insertNewRequest(reqid, userID, "", "", tags, title,
	// body, "", "", "", "", "");
	// dbQuery.updateWordCount(tags, body);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// // CHANGE THIS
	// return GSON.toJson(message);
	// }
	// }

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
	 *
	 * @author Jared
	 *
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