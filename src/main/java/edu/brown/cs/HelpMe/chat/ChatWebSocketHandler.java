package edu.brown.cs.HelpMe.chat;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

/**
 * Handles chat web socket
 */
@WebSocket
public class ChatWebSocketHandler {

	private String sender, msg;

	/**
	 * Message emitted when a user connects
	 * 
	 * @param user user
	 */
	@OnWebSocketConnect
	public void onConnect(Session user) throws Exception {
		String username = "User" + Chat.nextUserNumber++;
		Chat.userUsernameMap.put(user, username);
<<<<<<< HEAD
//		 Chat.broadcastMessage(sender = "Server",
//		 msg = (username + " joined the chat"));
=======
		Chat.broadcastMessage(sender = "Server",
				msg = (username + " joined the chat"));
>>>>>>> b44f0ee3de8de1dee0729a74d2401f8b40164966
	}

	/**
	 * message emitted on close 
	 * @param user user 
	 * @param statusCode status code
	 * @param reason reason
	 */
	@OnWebSocketClose
	public void onClose(Session user, int statusCode, String reason) {
		String username = Chat.userUsernameMap.get(user);
		Chat.userUsernameMap.remove(user);
<<<<<<< HEAD
//		 Chat.broadcastMessage(sender = "Server",
//		 msg = (username + " left the chat"));
=======
		Chat.broadcastMessage(sender = "Server",
				msg = (username + " left the chat"));
>>>>>>> b44f0ee3de8de1dee0729a74d2401f8b40164966
	}

	/**
	 * Emit message that user submits
	 * @param user user
	 * @param message message
	 */
	@OnWebSocketMessage
	public void onMessage(Session user, String message) {
		Chat.broadcastMessage(sender = Chat.userUsernameMap.get(user),
				msg = message);
	}

}