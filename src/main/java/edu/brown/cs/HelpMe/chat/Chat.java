package edu.brown.cs.HelpMe.chat;

import org.eclipse.jetty.websocket.api.*;
import org.json.*;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.text.*;
import java.util.*;
import static j2html.TagCreator.*;
import static spark.Spark.*;

/*
 * Class for the chat
 */
public class Chat {

	static Map<Session, String> userUsernameMap = new HashMap<>();
	static int nextUserNumber = 1; // Used for creating the next username

	public Chat() {
	}

	/**
	 * starts a web socket
	 */
	public void initializeSocket() {
		webSocket("/chat", ChatWebSocketHandler.class);
	}

	/**
	 * Starts the web socket
	 */
	public void startSocket() {
		init();
	}

	/**
	 * emits messages that the user submits
	 * @param sender user
	 * @param message message
	 */
	public static void broadcastMessage(String sender, String message) {
		userUsernameMap.keySet().stream().filter(Session::isOpen)
				.forEach(session -> {
					try {
						session.getRemote()
								.sendString(String.valueOf(new JSONObject()
										.put("userMessage",
												createHtmlMessageFromSender(
														sender, message))
										.put("userlist",
												userUsernameMap.values())));
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
	}

	/**
	 * creates html message from sender
	 * @param sender user
 	 * @param message message
	 * @return
	 */
	private static String createHtmlMessageFromSender(String sender,
			String message) {
		return article().withClass("user_" + sender).with(b(sender + ""),
				p(message),
				span().withClass("timestamp").withText(
						new SimpleDateFormat("HH:mm:ss").format(new Date())))
				.render();
	}

	/**
	 * Helps create html page for chat
	 */
	private static FreeMarkerEngine createEngine() {
		Configuration config = new Configuration();
		// Spark.externalStaticFileLocation(
		// "src/main/resources/spark/template/freemarker");
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

}