package org.programus.nxj.rockboy.core.io;

import java.awt.Point;

import lejos.nxt.ColorLightSensor;
import lejos.nxt.LCD;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

/**
 * PcIO is an IO module just for debug. 
 * @author Programus
 * 
 * @see IOModule
 *
 */
public class PcIO extends IOModule {
	
	private static Point limit = new Point(LCD.SCREEN_WIDTH, LCD.SCREEN_HEIGHT); 

	@Override
	public ColorLightSensor getColorLightSensor() {
		return null;
	}

	@Override
	public int getRotationAngle() {
		return 45;
	}

	@Override
	public TouchSensor getTouchSensor() {
		return null;
	}

	@Override
	public UltrasonicSensor getUltrasonicSensor() {
		return null;
	}

	@Override
	public boolean isTouchSensorPressed() {
		return false;
	}

	@Override
	public void resetPosition() {
	}

	@Override
	public Point getScreenBoundary() {
		return limit; 
	}

	@Override
	public void playTone(int freq, int duration) {
		// nothing now. 
	}

	@Override
	public int getDistance() {
		return 0;
	}

	@Override
	public boolean isTouchPressed() {
		return false;
	}

	@Override
	public int getLightValue() {
		return 50;
	}

	@Override
	public void setColorLightSensorType(int type) {
	}

}
