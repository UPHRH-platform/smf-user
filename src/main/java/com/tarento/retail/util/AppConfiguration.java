package com.tarento.retail.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

@Configuration
@SuppressWarnings("all")
@Getter
@PropertySource(value = { "/application.properties" })
public class AppConfiguration {

	@Value("${mail.smtp.host}")
	private String smtpHost;

	@Value("${mail.smtp.auth}")
	private String smtpAuth;

	@Value("${mail.smtp.port}")
	private String smtpPort;

	@Value("${mail.smtp.user}")
	private String smtpUser;

	@Value("${mail.smtp.password}")
	private String smtpPassword;

	@Value("${mail.smtp.email}")
	private String smtpEmail;

	@Value("${otp.validity.mins}")
	private int otpValidity;

	@Value("${jwt.validity.mins}")
	private int jwtValidity;

}
