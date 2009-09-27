package org.programus.nxj.pidbot.util;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class Consts {
	public static final Motor MOTOR = Motor.A; 
	public static final SensorPort SONAR_PORT = SensorPort.S2; 
	
	public static final int P = 30; 
	public static final int I = 0; 
	public static final int D = 0; 
	public static final int SCALE = 5; 
	
	public static final String PURSUE_FILE = "pursue_robot.wav"; 
	public static final String RUNAWAY_FILE = "runaway_robot.wav"; 
}
