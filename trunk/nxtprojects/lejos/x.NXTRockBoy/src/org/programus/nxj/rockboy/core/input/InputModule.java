package org.programus.nxj.rockboy.core.input;

import java.awt.Point;

import lejos.nxt.ColorLightSensor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

public abstract class InputModule {
	
	private static InputModule usingInput; 
	public synchronized static InputModule getInputModule() {
		if (usingInput == null) {
			try {
				Class.forName("java.lang.ClassLoader");
				usingInput = new PcInput(); 
			} catch (ClassNotFoundException e) {
				usingInput = new NXTInput(); 
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
}
