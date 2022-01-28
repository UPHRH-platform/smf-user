package com.tarento.retail.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	public static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

	private static AppConfiguration appConfiguration;
	private static Properties props = null;

	@Autowired
	private NotificationService(AppConfiguration appConfig) {
		appConfiguration = appConfig;

		props = System.getProperties();
		props.put("mail.smtp.host", appConfiguration.getSmtpHost());
		props.put("mail.smtp.socketFactory.port", appConfiguration.getSmtpPort());
		props.put("mail.smtp.port", appConfiguration.getSmtpPort());
		props.put("mail.smtp.auth", appConfiguration.getSmtpAuth());
		props.put("mail.smtp.starttls.enable", appConfiguration.getSmtpAuth());

	}

	private static final String SMTP = "smtp";

	/**
	 * this method is used to send email.
	 *
	 * @param receipent
	 *            email to whom we send mail
	 * @param context
	 *            VelocityContext
	 * @param templateName
	 *            String
	 * @param subject
	 *            subject
	 */
	@Async
	public static Boolean sendMail(String[] recipient, String subject, String body) {
		try {
			Session session = Session.getInstance(props,
					new GMailAuthenticator(appConfiguration.getSmtpUser(), appConfiguration.getSmtpPassword()));
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(appConfiguration.getSmtpEmail()));
			int size = recipient.length;
			int i = 0;
			while (size > 0) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient[i]));
				i++;
				size--;
			}
			message.setSubject(subject);
			message.setText(body);
			Transport transport = session.getTransport(SMTP);
			transport.connect(appConfiguration.getSmtpHost(), appConfiguration.getSmtpUser(),
					appConfiguration.getSmtpPassword());
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			return Boolean.TRUE;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(String.format("Exception in %s : %s", "sendMail", e.getMessage()));
		}
		return Boolean.FALSE;
	}

}
