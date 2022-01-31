package com.tarento.retail.util;

import java.io.StringWriter;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
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
	private static final String TEXT_HTML = "text/html";

	/**
	 * this method is used to send email.
	 *
	 * @param receipent
	 *            email to whom we send mail
	 * @param subject
	 *            subject
	 * @param body
	 *            body
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
	 * @return
	 */
	@Async
	public static Boolean sendMail(String[] receipent, String subject, VelocityContext context, String templateName) {
		try {
			Session session = Session.getInstance(props,
					new GMailAuthenticator(appConfiguration.getSmtpUser(), appConfiguration.getSmtpPassword()));
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(appConfiguration.getSmtpEmail()));
			int size = receipent.length;
			int i = 0;
			while (size > 0) {
				message.addRecipient(Message.RecipientType.BCC, new InternetAddress(receipent[i]));
				i++;
				size--;
			}
			message.setSubject(subject);
			VelocityEngine engine = new VelocityEngine();
			engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
			engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			engine.init();
			Template template = engine.getTemplate(templateName);
			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			message.setContent(writer.toString(), TEXT_HTML);
			Transport transport = session.getTransport(SMTP);
			transport.connect(appConfiguration.getSmtpHost(), appConfiguration.getSmtpUser(),
					appConfiguration.getSmtpPassword());
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			return Boolean.TRUE;
		} catch (Exception e) {
			LOGGER.error(String.format("Exception in %s : %s", "sendMail", e.getMessage()));
			return Boolean.FALSE;
		}
	}

}
