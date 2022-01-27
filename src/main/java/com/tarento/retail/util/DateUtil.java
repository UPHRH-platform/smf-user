package com.tarento.retail.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {

	public static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

	public static long getCurrentTimestamp() {
		return new Date().getTime();
	}

}
