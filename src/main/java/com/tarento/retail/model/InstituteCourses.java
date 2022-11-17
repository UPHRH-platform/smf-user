package com.tarento.retail.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class InstituteCourses {

	private Long id;
	private String districtName;
	private String centerCode;
	private String degree;
	private String course;
	private String appliedYear;
	private String sector;
	private Long profileId; //Fk UserProfile
	
	private Date createdDate;
	private Long createdBy;
	private Date updatedDate;
	private Long updatedBy;
	
}
