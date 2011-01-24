package org.programus.nxj.guessnumber.utils;

import java.util.Vector;

import lejos.nxt.ColorLightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.util.Delay;

public class BallScanner {
	private static final Motor MOVE_MOTOR = Motor.A; 
	
	private static final SensorPort COLOR_PORT = SensorPort.S2; 
	private static final SensorPort STOP_MOTOR_PORT = SensorPort.S3; 
	private static final SensorPort START_DETECT_PORT = SensorPort.S1; 
	
	private TouchSensor stopMotorSensor = new TouchSensor(STOP_MOTOR_PORT); 
	private TouchSensor startScanSensor = new TouchSensor(START_DETECT_PORT); 
	private ColorLightSensor colorSensor = 
		new ColorLightSensor(COLOR_PORT, ColorLightSensor.TYPE_COLORFULL); 
	
	private Vector colorCombinationContainer = new Vector(1); 
	private boolean keepScanning; 
	
	private Runnable scanTask = new Runnable() {
		public void run() {
			while(keepScanning) {
				for (int i = 0; i < 20 || !startScanSensor.isPressed();) {
					if (!keepScanning) {
						return;
					} else if (startScanSensor.isPressed()) {
						Delay.msDelay(25); 
						i++;
					} else {
						i = 0;
					}
				}
				int[] colors = new int[Constants.BALL_NUM]; 
				for (int i = 0; i < colors.length; i++) {
					MOVE_MOTOR.rotateTo(Constants.MOVE_STEP * i); 
					colors[i] = colorSensor.readValue(); 
				}
				MOVE_MOTOR.rotateTo(0); 
				while(true) {
					if (!keepScanning) {
						return;
					}
					if (colorCombinationContainer.isEmpty()) {
						colorCombinationContainer.addElement(new ColorCombination(colors)); 
						break;
					}
				}
				while(startScanSensor.isPressed()) {
					if (!keepScanning) {
						return; 
					}
				}
			}
		}
	}; 
	
	public void init() {
		MOVE_MOTOR.setSpeed(Constants.MOVE_SPEED); 
		MOVE_MOTOR.regulateSpeed(true); 
		if (!stopMotorSensor.isPressed()) {
			MOVE_MOTOR.backward(); 
			while (!stopMotorSensor.isPressed()); 
		}
		MOVE_MOTOR.stop(); 
		MOVE_MOTOR.resetTachoCount(); 
		Sound.beepSequenceUp();
	}
	
	public void startScanThread() {
		Thread scanThread = new Thread(scanTask); 
		scanThread.setDaemon(true); 
		keepScanning = true;
		scanThread.start(); 
	}
	
	public void stopScanThread() {
		keepScanning = false; 
	}
	
	public ColorCombination getColorCombination() {
		while (true) {
			if (!this.colorCombinationContainer.isEmpty()) {
				break;
			}
			Delay.msDelay(10); 
		}
		
		ColorCombination ret = (ColorCombination) this.colorCombinationContainer.elementAt(0); 
		this.colorCombinationContainer.clear(); 
		return ret;
	}

	/**
	 * @return the keepScanning
	 */
	public boolean isKeepScanning() {
		return keepScanning;
	}

	/**
	 * @param keepScanning the keepScanning to set
	 */
	public void setKeepScanning(boolean keepScanning) {
		this.keepScanning = keepScanning;
	}
}
