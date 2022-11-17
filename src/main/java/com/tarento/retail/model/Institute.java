package com.tarento.retail.model;

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
public class Institute {
	private String districtName;
	private String parentTrCenterCode;
	private String parentTrCenterName;
	private String degree;
	private String course;
	private String appliedYear;
	private String sector;
	private String emailId;
	
}
