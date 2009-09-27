package org.programus.nxj.music.utils;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

public class Consts {
	public static SensorPort TOUCH_P = SensorPort.S2; 
	public static SensorPort SONAR_P = SensorPort.S1; 
	public static TouchSensor TOUCH_S = new TouchSensor(TOUCH_P); 
	public static UltrasonicSensor SONAR_S = new UltrasonicSensor(SONAR_P); 
	
	public static Motor NOTE_MOTOR = Motor.C; 
	public static Motor PITCH_MOTOR = Motor.B; 
}
