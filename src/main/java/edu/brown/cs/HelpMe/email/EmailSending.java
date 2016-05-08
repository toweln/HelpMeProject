package edu.brown.cs.HelpMe.email;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import edu.brown.cs.HelpMe.main.SQLQueries;

public class EmailSending {

	private SQLQueries queries;

	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;

	public EmailSending() throws UnknownHostException {

	}

	public static void sendWelcomeEmail(String email)
			throws AddressException, MessagingException {
		String body = "Welcome to HelpMe! We hope you find the site useful!"
				+ "<br><br> Regards, <br>The HelpMe Team";
		generateAndSendEmail(email, "Welcome to HelpMe!", body);
	}

	public static void sendTuteeEmail(String email, String summary,
			String tutor, String link, String ratingLink)
			throws AddressException, MessagingException {
		String subject = tutor + " wants to help you!";
		String body = tutor + " wants to help you with " + summary
				+ "<br><br> Go to " + link
				+ " to chat! <br><br> When you're done, rate your tutor here: "
				+ ratingLink;
		generateAndSendEmail(email, subject, body);
	}

	public static void sendTutorEmail(String email, String summary,
			String tutee, String link)
			throws AddressException, MessagingException {
		String subject = "You are helping " + tutee;
		String body = "You are helping " + tutee + " with " + summary
				+ " <br><br> Go to " + link + " to chat!";
		generateAndSendEmail(email, subject, body);
	}

	// public static void sendRatingEmail(String email, String summary,
	// String tutor, String link)
	// throws AddressException, MessagingException {
	// String subject = "Rate your HelpMe Tutor";
	// String body = "How effective was " + tutor
	// + " as a tutor? Rate them here: <br><br> " + link
	// + " to improve our site";
	// generateAndSendEmail(email, subject, body);
	// }

	public static void generateAndSendEmail(String address, String subject,
			String body) throws AddressException, MessagingException {

		// Step1
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");

		// Step2
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO,
				new InternetAddress(address));
		generateMailMessage.setSubject(subject);
		generateMailMessage.setContent(body, "text/html");

		// Step3
		Transport transport = getMailSession.getTransport("smtp");

		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		transport.connect("smtp.gmail.com", "helpmebrown@gmail.com",
				"ilovecs32");
		transport.sendMessage(generateMailMessage,
				generateMailMessage.getAllRecipients());
		transport.close();
	}
}
