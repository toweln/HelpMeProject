package edu.brown.cs.HelpMe.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

/**
 * Sets up server for GUI.
 *
 * @author acj
 *
 */
public class GUIsetup {
	private static final Gson GSON = new Gson();

	public GUIsetup() {
		runSparkServer();
	}

	/**
	 * runs spark server
	 */
	private void runSparkServer() {
		Spark.externalStaticFileLocation("src/main/resources/static");

		FreeMarkerEngine freeMarker = createEngine();

		// Setup Spark Routes
		Spark.get("/helpme", new FrontHandler(), freeMarker);
		Spark.get("/signup.html", new SignUpDropdownHandler(), freeMarker);
		Spark.get("/home.html", new NewUserHandler(), freeMarker);
	}

	/**
	 * handles the front index home page where you log in
	 */
	private class FrontHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response res) {
			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
			return new ModelAndView(variables, "index.html");
		}
	}

	/*
	 * handles sign up page
	 */
	private class SignUpDropdownHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response res) {
			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
			return new ModelAndView(variables, "signup.html");
		}
	}

	/**
	 * handler for new users.
	 *
	 * @author acj
	 *
	 */
	private class NewUserHandler implements TemplateViewRoute {
		@Override
		public ModelAndView handle(Request req, Response res) {

			QueryParamsMap qm = req.queryMap();

			String username = qm.value("username");
			String name = qm.value("name");
			int phone = Integer
					.parseInt(qm.value("phone").replaceAll("[\\s\\-()]", ""));
			String password1 = qm.value("password1");
			String password2 = qm.value("password2");
			String topics = qm.value("topics");
			System.out.println(username);
			System.out.println(name);
			System.out.println(phone);
			System.out.println(password1);
			System.out.println(password2);
			System.out.println(topics);

			Map<String, String> variables = ImmutableMap.of("title", "HelpMe!");
			return new ModelAndView(variables, "home.html");
		}
	}

	/**
	 * something about engines
	 */
	private static FreeMarkerEngine createEngine() {
		Configuration config = new Configuration();
		File templates = new File(
				"src/main/resources/spark/template/freemarker");
		try {
			config.setDirectoryForTemplateLoading(templates);
		} catch (IOException ioe) {
			System.out.printf(
					"ERROR: Unable use %s " + "for template loading.%n",
					templates);
			System.exit(1);
		}
		return new FreeMarkerEngine(config);
	}
}
