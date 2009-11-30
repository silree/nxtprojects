package org.programus.nxj.rockboy.core.io;

import java.awt.Point;

import lejos.nxt.ColorLightSensor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

/**
 * This class is an abstract super class for all IO module class. 
 * There is two IO module now: NXTIO and PcIO. 
 * The NXTIO is a class to provide real NXTIO function; 
 * while the PcIO is just a class for debugging some platform independence code on PC. 
 * 
 * @author Programus
 * @see NXTIO
 * @see PcIO
 */
public abstract class IOModule {
	
	private static IOModule usingInput; 
	
	/**
	 * Return the IO module which is suitable in current environment. 
	 * @return the IO module which is suitable
	 */
	public synchronized static IOModule getIOModule() {
		if (usingInput == null) {
			try {
				// In NXT environment, there is no java.lang.ClassLoader class. 
				Class.forName("java.lang.ClassLoader");
				usingInput = new PcIO(); 
			} catch (ClassNotFoundException e) {
				usingInput = new NXTIO(); 
			} 
		}
		return usingInput; 
	}
	
	/**
	 * Return the rotation angle in angle. 
	 * Turn clockwise will be positive while counter clockwise will be negative. 
	 * @return the rotation angle in angle. 
	 */
	public abstract int getRotationAngle(); 
	
	/**
	 * Reset the NXT position. 
	 * Just make sure NXT is in the starting position. 
	 */
	public abstract void resetPosition(); 
	
	public abstract TouchSensor getTouchSensor(); 
	public abstract ColorLightSensor getColorLightSensor(); 
	public abstract UltrasonicSensor getUltrasonicSensor(); 
	
	public abstract boolean isTouchSensorPressed(); 
	
	public abstract Point getScreenBoundary(); 
	
	public abstract void playTone(int freq, int duration); 
	
	public abstract int getDistance(); 
	
	public abstract boolean isTouchPressed(); 
	
	public abstract void setColorLightSensorType(int type); 
	/**
	 * Return the light value in percentage. 
	 * It can detect whether you connect with a light sensor or color light sensor automatically. 
	 * @return light value, 0-100
	 */
	public abstract int getLightValue(); 
}
