package edu.brown.cs.HelpMe.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Map;

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

import freemarker.template.Configuration;

public class Main {
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;
  private static SQLQueries dbQuery;
  private static final Gson GSON = new Gson();


  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
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

    if (options.has("gui")) {
      runSparkServer();
    } else {
      // Process commands
    }
  }

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

  private void runSparkServer() {
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/", new FrontHandler(), freeMarker);
    //SPARK REQUESTS I WROTE --JARED
    Spark.post("/login", new LoginHandler());
  }

  private class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "HelpMe!");
      return new ModelAndView(variables, "query.ftl");
    }
  }

  //SPARK HANDLER I WROTE --JARED
  private static class LoginHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String userName = qm.value("username");
      String password = qm.value("password");
      Boolean status = false;
      try {
        status = dbQuery.certifyLogin(userName, password);
      } catch (SQLException e) {
        e.printStackTrace();
      }


      return GSON.toJson(status);
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
