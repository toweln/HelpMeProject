//package edu.brown.cs.HelpMe.main;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//import joptsimple.OptionParser;
//import joptsimple.OptionSet;
//import spark.ExceptionHandler;
//import spark.ModelAndView;
//import spark.QueryParamsMap;
//import spark.Request;
//import spark.Response;
//import spark.Route;
//import spark.Spark;
//import spark.TemplateViewRoute;
//import spark.template.freemarker.FreeMarkerEngine;
//
//import com.google.common.collect.ImmutableMap;
//import com.google.gson.Gson;
//
//import edu.brown.cs.HelpMe.autocorrect.CommandParser;
//import edu.brown.cs.HelpMe.autocorrect.SuggestionGenerator;
//import freemarker.template.Configuration;
//
//public class Main_before_pull {
//	public static void main(String[] args) {
//		new Main_before_pull(args).run();
//	}
//
//	private String[] args;
//	private static SQLQueries dbQuery;
//	private static final Gson GSON = new Gson();
//	private CommandParser cp;
//	private static SuggestionGenerator sg;
//	private User currentUser;
//	private static String userID;
//
//	private Main_before_pull(String[] args) {
//		this.args = args;
//	}
//
//	private void run() {
//		OptionParser parser = new OptionParser();
//		String database = "smallDb.db";
//		try {
//			dbQuery = new SQLQueries(database);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		parser.accepts("gui");
//		OptionSet options = parser.parse(args);
//
//		if (options.has("gui")) {
//			this.cp = new CommandParser(false, 2, true, false, false);
//			this.sg = null;
//			try {
//				TagDatabase td = new TagDatabase();
//				this.sg = new SuggestionGenerator(td, cp);
//			} catch (FileNotFoundException e) {
//				System.out.println("ERROR: Please specify a text file.");
//				System.exit(1);
//			} catch (SQLException e) {
//				System.out.println("ERROR: Database does not exist");
//			} catch (ClassNotFoundException e) {
//				System.out.println("ERROR: Cannot find table");
//			}
//			runSparkServer();
//		} else {
//			// Process commands
//		}
//	}
//
//	private static FreeMarkerEngine createEngine() {
//		Configuration config = new Configuration();
//		File templates = new File(
//				"src/main/resources/spark/template/freemarker");
//		try {
//			config.setDirectoryForTemplateLoading(templates);
//		} catch (IOException ioe) {
//			System.out.printf("ERROR: Unable use %s for template loading.\n",
//					templates);
//			System.exit(1);
//		}
//		return new FreeMarkerEngine(config);
//	}
//
//	private void runSparkServer() {
//		Spark.externalStaticFileLocation("src/main/resources/static");
//		Spark.exception(Exception.class, new ExceptionPrinter());
//
//		FreeMarkerEngine freeMarker = createEngine();
//
//		// Setup Spark Routes
//		Spark.get("/", new FrontHandler(), freeMarker);
//		// SPARK REQUESTS I WROTE --JARED
//		Spark.post("/login", new LoginHandler());
//		Spark.post("/suggest", new SuggestHandler());
//    //Spark.post("/submitQuestion", new SubmitQuestionHandler());
//    Spark.post("/newUser", new signupHandler());
//
//
//		Spark.get("/signup.html", new SignupDropdownHandler(), freeMarker);
//		Spark.get("/home.html", new HomeHandler(), freeMarker);
//		Spark.get("/leaderboard", new LeaderboardHandler(), freeMarker);
//		Spark.get("/q_new.html", new NewQuestionHandler(), freeMarker);
//		Spark.get("/q.html", new SubmittedQuestion(), freeMarker);
//	}
//
//	private class FrontHandler implements TemplateViewRoute {
//		@Override
//		public ModelAndView handle(Request req, Response res) {
//
//			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
//			return new ModelAndView(variables, "index.html");
//		}
//	}
//
//	private class HomeHandler implements TemplateViewRoute {
//		@Override
//		public ModelAndView handle(Request req, Response res) {
//
//			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
//			return new ModelAndView(variables, "home.html");
//		}
//	}
//
//	private class LeaderboardHandler implements TemplateViewRoute {
//		@Override
//		public ModelAndView handle(Request req, Response res) {
//
//			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
//			return new ModelAndView(variables, "leaderboard.html");
//		}
//	}
//
//	private class SignupDropdownHandler implements TemplateViewRoute {
//		@Override
//		public ModelAndView handle(Request req, Response res) {
//			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
//			return new ModelAndView(variables, "signup.html");
//		}
//	}
//
//	private class NewQuestionHandler implements TemplateViewRoute {
//		@Override
//		public ModelAndView handle(Request req, Response res) {
//
//			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
//			return new ModelAndView(variables, "q_new.html");
//		}
//	}
//
//	private class SubmittedQuestion implements TemplateViewRoute {
//		@Override
//		public ModelAndView handle(Request req, Response res) {
//
//			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
//			return new ModelAndView(variables, "q.html");
//		}
//	}
//
//	// SPARK HANDLER I WROTE --JARED
//	private static class LoginHandler implements Route {
//		@Override
//		public Object handle(Request req, Response res) {
//			QueryParamsMap qm = req.queryMap();
//			String userName = qm.value("username");
//			String password = qm.value("password");
//			userName = userName.substring(1, userName.length() - 1);
//			password = password.substring(1, password.length() - 1);
//			System.out.println(userName);
//			System.out.println(password);
//			Boolean status = false;
//			try {
//				status = dbQuery.certifyLogin(userName, password);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//
//			return GSON.toJson(status);
//		}
//	}
//	/**
//   * Handler for handling signups.
//   * @author Jared
//   *
//   */
//   private static class signupHandler implements Route {
//      @Override
//      public Object handle(Request req, Response res) {
//        QueryParamsMap qm = req.queryMap();
//        String userName = qm.value("username");
//        String password = qm.value("password");
//        String first = qm.value("first_name");
//        String last = qm.value("last_name");
//        String email = qm.value("email");
//        String phone = qm.value("phone");
//
//        userName = userName.substring(1, userName.length() - 1);
//        password = password.substring(1, password.length() - 1);
//        first = first.substring(1, first.length() - 1);
//        last = last.substring(1, last.length() - 1);
//        email = email.substring(1, email.length() - 1);
//        phone = phone.substring(1, phone.length() - 1);
//        UUID newID = UUID.randomUUID();
//        Boolean status = false;
//        try {
//        dbQuery.insertNewUser(newID.toString(), first, last, email, phone,
//            userName, password);
//          status = true;
//        } catch (SQLException e) {
//          e.printStackTrace();
//        }
//
//        return GSON.toJson(status);
//      }
//    }
//	private static class SubmitQuestionHandler implements Route {
//		@Override
//		public Object handle(Request req, Response res) {
//			QueryParamsMap qm = req.queryMap();
//			String title = qm.value("title");
//			String rawTags = qm.value("tags");
//			String body = qm.value("message");
//			List<String> tags = Arrays.asList(rawTags.split("\\s*,\\s*"));
//			String message = qm.value("message");
//			String reqid = UUID.randomUUID().toString();
//			try {
//        dbQuery.insertNewRequest(reqid, userID, "", "", tags, title, body, "", "", "", "", "");
//      } catch (SQLException e) {
//        e.printStackTrace();
//      }
//			// CHANGE THIS
//			return GSON.toJson(message);
//		}
//	}
//
//	private static class SuggestHandler implements Route {
//		@Override
//		public Object handle(Request req, Response res) {
//			QueryParamsMap qm = req.queryMap();
//			String userInput = qm.value("input");
//			List<String> suggestions = new ArrayList<>();
//			if (userInput.length() == 0) {
//				return suggestions;
//			}
//			suggestions = sg.getSuggestions(userInput);
//			List<String> capSuggs = new ArrayList<>();
//			for (String sugg : suggestions) {
//				StringBuffer sb = new StringBuffer();
//
//				String[] split = sugg.split(" ");
//				for (String str : split) {
//					char[] stringArray = str.trim().toCharArray();
//					stringArray[0] = Character.toUpperCase(stringArray[0]);
//					str = new String(stringArray);
//
//					sb.append(str).append(" ");
//				}
//				capSuggs.add(sb.toString());
//			}
//			Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
//					.put("suggestions", capSuggs).build();
//			return GSON.toJson(variables);
//		}
//	}
//
//	private static class ExceptionPrinter implements ExceptionHandler {
//		@Override
//		public void handle(Exception e, Request req, Response res) {
//			res.status(500);
//			StringWriter stacktrace = new StringWriter();
//			try (PrintWriter pw = new PrintWriter(stacktrace)) {
//				pw.println("<pre>");
//				e.printStackTrace(pw);
//				pw.println("</pre>");
//			}
//			res.body(stacktrace.toString());
//		}
//	}
//
//}