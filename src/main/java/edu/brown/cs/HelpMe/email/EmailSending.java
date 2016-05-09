package edu.brown.cs.HelpMe.email;

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

/**
 * Class for handling email notifications.
 * @author Jared
 */
public class EmailSending {

	private SQLQueries queries;
	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;

	/**
	 * Constructor for the EmailSending.
	 * @throws UnknownHostException
	 */
	public EmailSending() throws UnknownHostException {

	}

	/**
	 * Sends a welcome email when the user creates an account.
	 * @param email email address to send welcome message to.
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void sendWelcomeEmail(String email)
			throws AddressException, MessagingException {
		String body = "Welcome to HelpMe! We hope you find the site useful!"
				+ "<br><br> Regards, <br>The HelpMe Team";
		generateAndSendEmail(email, "Welcome to HelpMe!", body);
	}

	/**
	 * Send email to a tutee.
	 * @param email Email address to send to
	 * @param summary Question summary
	 * @param tutor name of the tutor
	 * @param link the link to the chatroom
	 * @param ratingLink the link to the rating page.
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void sendTuteeEmail(String email, String summary,
			String tutor, String link, String ratingLink)
			throws AddressException, MessagingException {
		String subject = tutor + " wants to help you!";
		String body = tutor + " wants to help you with " + summary
				+ "<br><br> Go to" + link
				+ " to chat! <br><br> When you're done, rate your tutor here: "
				+ ratingLink;
		generateAndSendEmail(email, subject, body);
	}

	/**
	 * Send email to a tutor.
	 * @param email email address to send email to.
	 * @param summary question summary.
	 * @param tutee the name of the tutee.
	 * @param link the link to the chatroom.
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void sendTutorEmail(String email, String summary,
			String tutee, String link)
			throws AddressException, MessagingException {
		String subject = "You are helping " + tutee;
		String body = "You are helping " + tutee + " with " + summary
				+ " <br><br> Go to " + link + " to chat!";
		generateAndSendEmail(email, subject, body);
  }

  /**
   * Method which handles mailserver connections and actually does the sending.
   * @param address email address to send the mail to
   * @param subject email subject line
   * @param body body of the email
   * @throws AddressException
   * @throws MessagingException
   */
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
    transport.connect("smtp.gmail.com", "helpmebrown@gmail.com", "ilovecs32");
    transport.sendMessage(generateMailMessage,
        generateMailMessage.getAllRecipients());
    transport.close();
  }
}
