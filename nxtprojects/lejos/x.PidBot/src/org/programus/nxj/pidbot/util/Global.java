package org.programus.nxj.pidbot.util;

import lejos.nxt.UltrasonicSensor;

public class Global {
	public static int traceDistance = -1; 
	public static UltrasonicSensor SONAR_SENSOR; 
	
	public static void sleep(long ms) {
		long time = System.currentTimeMillis() + ms; 
		while(System.currentTimeMillis() <= time); 
	}
}
