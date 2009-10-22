package org.programus.nxj.util;

public class TimeUtil {
	public static int[] getHMSms(long time) {
		int ms = (int) (time % 1000); 
		int sec = (int) (time % 60000) / 1000; 
		int min = (int) (time % 3600000) / 60000; 
		int hour = (int) (time / 3600000); 
		
		return new int[] {hour, min, sec, ms}; 
	}
}
