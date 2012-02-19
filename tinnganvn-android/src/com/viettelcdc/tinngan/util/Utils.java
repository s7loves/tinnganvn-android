package com.viettelcdc.tinngan.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public abstract class Utils {
	public static String fromUtf8(String string) throws UnsupportedEncodingException {
	    byte[] utf8 = string.getBytes("UTF-8");
	    return new String(utf8, "UTF-8");
	}
	
	public static long MSECS_TO_HOURS = 1000*60*60;
	
	public static String formatDate(Date date) {
		long currentTime = System.currentTimeMillis();
		long hours = currentTime - date.getTime();
		hours /= MSECS_TO_HOURS;
		if(hours > 24)
			return date.getDate() + "/" + (date.getMonth() + 1);
		return hours + " giờ trước";
	}
	
	static public String msToClockTime(long milliseconds){
		long seconds = milliseconds / 1000;
		int minutes = (int)seconds / 60;
		seconds %= 60;
		StringBuilder sb = new StringBuilder();
		sb.append(twoDigit(minutes)).append(':');
		sb.append(twoDigit((int)seconds));
		return sb.toString();
	}
	
	static public String twoDigit(int d){
		NumberFormat formatter = new DecimalFormat("#00");
		return formatter.format(d);
	}
	
	public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
	
	static public String wrapByHtmlTag(String content, String baseUrl){
		final String HEADER = 
			"<html><head><meta content='text/html; charset=utf-8'/>" +
			"<base href='" + baseUrl + "' />" +
			"</head><body style='align:justify;'>";
		final String FOOTER = "</body></html>";
		return HEADER + content + FOOTER;
	}
	
	static public boolean isConnectedToInternet(Context context) {
		ConnectivityManager conMgr =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr == null) return false;
		NetworkInfo internet = conMgr.getActiveNetworkInfo();
		if (internet == null) return false;
		
		return (internet.isConnected() && internet.isAvailable());
	}
}
