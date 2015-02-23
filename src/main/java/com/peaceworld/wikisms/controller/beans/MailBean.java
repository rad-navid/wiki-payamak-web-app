package com.peaceworld.wikisms.controller.beans;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.*;

@Stateless
public class MailBean {
	
	public boolean generateAndSendEmail(String to,String content,String subject)
	{
		// Sender's email ID needs to be mentioned
		String from = "peaceworld.corporation@yahoo.com";
		String pass = "suimxzui$";
		// Recipient's email ID needs to be mentioned.
		String host = "smtp.mail.yahoo.com";

		// Get system properties
		Properties properties = System.getProperties();
		// Setup mail server
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.user", from);
		properties.put("mail.smtp.password", pass);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			
			 //set message headers
			message.addHeader("Content-type", "text/html; charset=UTF-8");
			//message.addHeader("format", "flowed");
			//message.addHeader("Content-Transfer-Encoding", "8bit");

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from, "ویکی پیامک","UTF-8"));
			message.setReplyTo(InternetAddress.parse("no.reply@wikipayam.com", false));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			message.setSubject(subject, "UTF-8");
			//message.setText(content, "UTF-8");
			message.setContent(content, "text/html; charset=UTF-8");
			message.setContentLanguage(new String[]{"fa"});
	 
			message.setSentDate(new Date());

			// Send message
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			return true;
		} catch (MessagingException mex) {
			//mex.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		}
		
		return false;
	}
}