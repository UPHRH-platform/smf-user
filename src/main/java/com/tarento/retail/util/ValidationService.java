package com.tarento.retail.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tarento.retail.model.UserProfile;
import com.tarento.retail.service.UserService;

@Service
public class ValidationService {

	@Autowired
	UserService userService;

	public String validateUserUpsert(UserProfile profile) {
		if (profile != null) {
			if (StringUtils.isBlank(profile.getEmailId())) {
				return ResponseMessages.ErrorMessages.EMAIL_MANDATORY;
			}
			Long userId = userService.checkUserNameExists(profile.getEmailId(), profile.getPhoneNo());
			if (userId != null && userId > 0 && profile.getId() == null) {
				return ResponseMessages.ErrorMessages.EMAIL_PHONE_ALREADY_EXISTS;
			}
			if ((profile.getId() != null && profile.getId() > 0 && userId > 0 && !userId.equals(profile.getId()))) {
				return ResponseMessages.ErrorMessages.EMAIL_PHONE_ALREADY_EXISTS;
			}
			return Constants.SUCCESS;
		}
		return ResponseMessages.ErrorMessages.CHECK_REQUEST_PARAMS;
	}

}
