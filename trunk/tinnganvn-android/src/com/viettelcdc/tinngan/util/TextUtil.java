package com.viettelcdc.tinngan.util;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

public abstract class TextUtil {
	public static String fromUtf8(String string) throws UnsupportedEncodingException {
	    byte[] utf8 = string.getBytes("UTF-8");
	    return new String(utf8, "UTF-8");
	}
	
	public static long MSECS_TO_HOURS = 1000*60*60;
	
	public static String countHours(Date date, String sufix) {
		long currentTime = System.currentTimeMillis();
		long hours = currentTime - date.getTime();
		hours /= MSECS_TO_HOURS;
		if(hours > 24) return date.getDate() + "/" + date.getMonth();
		return hours + sufix;
	}
}
