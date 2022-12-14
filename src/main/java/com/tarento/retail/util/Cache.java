package com.tarento.retail.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tarento.retail.model.LoginAuthentication;

@Service
public class Cache {

	private static AppConfiguration appConfiguration;

	@Autowired
	private Cache(AppConfiguration appConfig) {
		appConfiguration = appConfig;
	}

	private static Map<String, LoginAuthentication> userAuthData = new HashMap<>();

	public static LoginAuthentication getUserAuthData(String username) {
		if (userAuthData.containsKey(username)) {
			return userAuthData.get(username);
		}
		return null;
	}

	/**
	 * Caches the user wise otp data with the expiry time
	 * 
	 * @param username
	 *            String
	 * @param otp
	 *            String
	 */
	public static void setUserOTPData(String username, String otp) {
		// set expiry time for 5 mins
		Long expiryTime = DateUtil.getCurrentTimestamp() + (appConfiguration.getOtpValidity() * 60 * 1000);

		LoginAuthentication loginAuth = new LoginAuthentication();
		loginAuth.setUsername(username);
		loginAuth.setOtp(otp);
		loginAuth.setOtpExpiryDate(expiryTime);

		userAuthData.put(username, loginAuth);
	}

	/**
	 * Caches user token and its expiry date
	 * 
	 * @param username
	 *            String
	 * @param token
	 *            String
	 */
	public static void setTokenDetails(String username, String token) {
		Long expiryTime = DateUtil.getCurrentTimestamp() + (appConfiguration.getJwtValidity() * 60 * 1000);

		LoginAuthentication loginAuth = new LoginAuthentication();
		if (getUserAuthData(username) != null) {
			loginAuth = userAuthData.get(username);
		}
		loginAuth.setUsername(username);
		loginAuth.setUserToken(token);
		loginAuth.setTokenExpiryDate(expiryTime);

		userAuthData.put(username, loginAuth);
	}

}
