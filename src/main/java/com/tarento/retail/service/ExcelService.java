package com.tarento.retail.service;

import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {

	public void bulkCreateInstitute(MultipartFile file, String xUserInfo, Long roleId);
	
}
