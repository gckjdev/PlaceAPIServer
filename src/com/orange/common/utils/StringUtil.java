package com.orange.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;

public class StringUtil {

	//format example "dd/MM/yyyy-hh:mm:ss"
	public static String dateToStringByFormat(Date date, String format){
		if (date == null || format == null)
			return null;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat (format);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
	}
	
	public static Date dateFromStringByFormat(String dateString, String format){
		if (dateString == null || format == null)
			return null;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat (format);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}
		
	public static String dateDescription(Date date){
		if (date == null)
			return "(null)";
		else
			return date.toString();
	}
	
	public static String md5base64encode(String input) {
		try {
			if (input == null)
				return null;

			if (input.length() == 0)
				return null;

			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes("UTF-8"));
			byte[] enc = md.digest();
			String base64str = Base64.encodeBase64String(enc);
			return base64str;
			
		} 
		catch (NoSuchAlgorithmException e) {
			return null;			
		} 
		catch (UnsupportedEncodingException e) {
			return null;
		}
		
	}
	
	public static String[] getStringList(String... stringList){
		return stringList;
	}
}
