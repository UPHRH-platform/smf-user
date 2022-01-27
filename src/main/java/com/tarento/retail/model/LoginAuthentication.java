package com.tarento.retail.model;

public class LoginAuthentication {

	private String username;
	private String otp;
	private Long otpExpiryDate;
	private String userToken;
	private Long tokenExpiryDate;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Long getOtpExpiryDate() {
		return otpExpiryDate;
	}

	public void setOtpExpiryDate(Long otpExpiryDate) {
		this.otpExpiryDate = otpExpiryDate;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public Long getTokenExpiryDate() {
		return tokenExpiryDate;
	}

	public void setTokenExpiryDate(Long tokenExpiryDate) {
		this.tokenExpiryDate = tokenExpiryDate;
	}

}
