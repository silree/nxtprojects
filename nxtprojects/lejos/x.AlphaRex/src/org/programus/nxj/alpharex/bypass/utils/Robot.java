package org.programus.nxj.alpharex.bypass.utils;

import lejos.nxt.ColorLightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

public class Robot {
	// Motors
	public static final Motor L_LEG_MOTOR = Motor.C; 
	public static final Motor R_LEG_MOTOR = Motor.B; 
	public static final Motor HEAD_MOTOR = Motor.A; 
	
	// Ports
	public static final SensorPort L_TOUCH_PORT = SensorPort.S2; 
	public static final SensorPort R_TOUCH_PORT = SensorPort.S1; 
	public static final SensorPort COLOR_PORT = SensorPort.S4; 
	public static final SensorPort SONAR_PORT = SensorPort.S3; 
	
	// Sensors
	public static final TouchSensor L_TOUCH = new TouchSensor(L_TOUCH_PORT);
	public static final TouchSensor R_TOUCH = new TouchSensor(R_TOUCH_PORT); 
	public static final ColorLightSensor COLOR_S = new ColorLightSensor(COLOR_PORT, ColorLightSensor.TYPE_COLORNONE); 
	public static final UltrasonicSensor SONAR_S = new UltrasonicSensor(SONAR_PORT); 

	public static final int WALK_SPEED = 360; 
	public static final int TURN_SPEED = WALK_SPEED + 90;
	public static final int ADJUST_SPEED = 180;
	public static final int HEAD_SPEED = WALK_SPEED * 5 / 3;
	public static final int HEAD_TURN_SPEED = TURN_SPEED * 5 / 3;
	public static final int FIND_SPEED = HEAD_SPEED >> 1;
	
	public static final int OFFSET_ANGLE = 90;
	public static final int TURN_OFFSET = 216 - 90;
	public static final int HEAD_LOOP_ANGLE = 360 * 5 / 3; 
	
	public static final int TURN_ROTATION = 20;
	
	public static final int STRAIGHT = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2; 
	
	public static final int NEAR = 30; 
	
	public static final String WAY_FILE = "WayPls.wav"; 
	public static final int WAY_FILE_LEN = 700;
	public static final String GO_FILE = "IGo.wav"; 
	public static final int GO_FILE_LEN = 2180; 
	public static final String THANKS_FILE = "Thanks.wav"; 
	public static final int ALARM_LEN = 200; 
}
