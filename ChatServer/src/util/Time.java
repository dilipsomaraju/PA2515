package util;

import java.text.SimpleDateFormat;

public class Time {
	public static String getTime(){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
		String time = sDateFormat.format(new java.util.Date()); 
		return time;
	}
	public static String getTime(String time){
		String str = time.replace("-","");
		str = str.replace(":","");
		str = str.replace(" ","");
		return str;
	}
}
