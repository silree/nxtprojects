package org.programus.nxj.rockboy.core.io;

import java.awt.Point;

import lejos.nxt.ColorLightSensor;
import lejos.nxt.LCD;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

public class PcIO extends IOModule {
	
	private static Point limit = new Point(LCD.SCREEN_WIDTH, LCD.SCREEN_HEIGHT); 

	@Override
	public ColorLightSensor getColorLightSensor() {
		return null;
	}

	@Override
	public int getRotationAngle() {
		return 0;
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
	public Point getScreenBound() {
		return limit; 
	}

	@Override
	public void playTone(int freq, int duration) {
		// nothing now. 
	}

}