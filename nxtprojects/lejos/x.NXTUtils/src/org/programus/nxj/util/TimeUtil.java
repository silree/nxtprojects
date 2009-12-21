package org.programus.nxj.util;

/**
 * A collection of some time related functions
 * @author Programus
 *
 */
public class TimeUtil {
	/**
	 * Convert long type time to hours, minutes, seconds. 
	 * @param time the time value of long type
	 * @return a 4 elements integer array: 0 - hours, 1 - minutes, 2 - seconds, 3 - microseconds
	 */
	public static int[] getHMSms(long time) {
		int ms = (int) (time % 1000); 
		int sec = (int) (time % 60000) / 1000; 
		int min = (int) (time % 3600000) / 60000; 
		int hour = (int) (time / 3600000); 
		
		return new int[] {hour, min, sec, ms}; 
	}
}
