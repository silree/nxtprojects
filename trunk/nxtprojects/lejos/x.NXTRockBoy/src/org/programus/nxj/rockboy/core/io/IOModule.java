package org.programus.nxj.rockboy.core.io;

import java.awt.Point;

import lejos.nxt.ColorLightSensor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

public abstract class IOModule {
	
	private static IOModule usingInput; 
	public synchronized static IOModule getIOModule() {
		if (usingInput == null) {
			try {
				Class.forName("java.lang.ClassLoader");
				usingInput = new PcIO(); 
			} catch (ClassNotFoundException e) {
				usingInput = new NXTIO(); 
			} 
		}
		return usingInput; 
	}
	
	public abstract int getRotationAngle(); 
	public abstract void resetPosition(); 
	
	public abstract TouchSensor getTouchSensor(); 
	public abstract ColorLightSensor getColorLightSensor(); 
	public abstract UltrasonicSensor getUltrasonicSensor(); 
	
	public abstract boolean isTouchSensorPressed(); 
	
	public abstract Point getScreenBound(); 
	
	public abstract void playTone(int freq, int duration); 
	
	public abstract int getDistance(); 
	
	public abstract boolean isTouchPressed(); 
}
