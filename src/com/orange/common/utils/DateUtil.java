package com.orange.common.utils;

import java.util.Date;

public class DateUtil {

	public static String currentDate(){
		Date date = new Date();
		return String.valueOf(date.getTime());
	}
}
