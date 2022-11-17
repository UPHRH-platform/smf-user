package com.tarento.retail.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.tarento.retail.dto.UserCountryDto;
import com.tarento.retail.model.Country;
import com.tarento.retail.model.Institute;
import com.tarento.retail.model.InstituteCourses;
import com.tarento.retail.model.User;
import com.tarento.retail.model.UserProfile;
import com.tarento.retail.service.ExcelService;
import com.tarento.retail.service.UserService;
import com.tarento.retail.util.Constants;
import com.tarento.retail.util.ExcelHelper;

@Service(value = Constants.EXCEL_SERVICE)
public class ExcelServiceImpl implements ExcelService{
	
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(ExcelServiceImpl.class);
	
	@Autowired
	private UserService userService;

	@Override
	public void bulkCreateInstitute(MultipartFile file, String xUserInfo, Long roleId) {
		try {
			List<Institute> institutes = ExcelHelper.excelToInstitutes(file.getInputStream());
			User creatorUser = new Gson().fromJson(xUserInfo, User.class);
			for (Institute institute : institutes) {

				UserProfile userProfile = userService.getUserProfile(institute.getEmailId());

				if (userProfile != null) {
					logger.info("institute profile exists  : " + institute.getEmailId());
					// update course
					saveInstituteCourse(institute,userProfile);

				} else {
					logger.info("institute profile exists : " + institute.getEmailId());
					UserProfile profile = new UserProfile();
					profile.setEmailId(institute.getEmailId());
					profile.setUsername(institute.getEmailId());
					profile.setFirstName(institute.getParentTrCenterName());
					profile.setCreatedBy(creatorUser.getId());
					profile.setUpdatedBy(creatorUser.getId());
					profile.setOrgId(creatorUser.getOrgId());
					profile.setUsername(institute.getEmailId());
					List<Long> roleIds = new ArrayList<>();
					roleIds.add(new Long(roleId));
					profile.setRoleId(roleIds);
					profile = userService.saveUserProfile(profile);

					// update user country
					if (profile != null && profile.getCountryId() != null) {
						UserCountryDto userCountryDto = new UserCountryDto();
						userCountryDto.setUserId(profile.getId());
						List<Country> country = new ArrayList<>();
						Country c = new Country();
						c.setId(profile.getCountryId());
						country.add(c);
						userCountryDto.setCountries(country);
						if (!userService.mapUserToCountry(userCountryDto)) {
							logger.error("Failed to update user country");
						}
					}
					// update course
					saveInstituteCourse(institute,profile);
				}
			}

		} catch (IOException e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}

	}
	
	private void saveInstituteCourse(Institute institute, UserProfile profile) {
		
		InstituteCourses InstituteCourses = userService.getInstituteCourses(profile.getId(), institute.getCourse(), institute.getDegree());
		if(InstituteCourses == null ) {
			InstituteCourses instituteCourse = new InstituteCourses();
			instituteCourse.setDistrictName(institute.getDistrictName());
			instituteCourse.setCenterCode(institute.getParentTrCenterCode());
			instituteCourse.setDegree(institute.getDegree());
			instituteCourse.setCourse(institute.getCourse());
			instituteCourse.setAppliedYear(institute.getAppliedYear());
			instituteCourse.setSector(institute.getSector());
			instituteCourse.setProfileId(profile.getId());
			instituteCourse.setCreatedBy(profile.getCreatedBy());
			userService.saveInstituteCourse(instituteCourse);
		}
	
	}

}
