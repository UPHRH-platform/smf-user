package com.tarento.retail.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUser {

	private String username;
	private String password;
	private String phoneNo;
	private String organization;
	private String otp;
	private Boolean isMobile;
	private int pin;
}
