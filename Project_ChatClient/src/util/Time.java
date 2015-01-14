package util;

import java.text.SimpleDateFormat;

public class Time {
	public static String getTime(){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
		String time = sDateFormat.format(new java.util.Date()); 
		return time;
	}
}
