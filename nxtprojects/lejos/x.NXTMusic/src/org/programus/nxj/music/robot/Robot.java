package org.programus.nxj.music.robot;

import lejos.nxt.LCD;
import lejos.nxt.Sound;

import org.programus.nxj.music.utils.Consts;
import org.programus.nxj.music.utils.MusicConsts;

public class Robot {
	
	private static int RISING_VAL = 35; 
	private static int NOTE_ROTATION_OFFSET = 22; 
	private static int NOTE_RANGE = 45; 
	private static int PITCH_ROTATION_OFFSET = 30 - 3; 
	private static int PITCH_RANGE = 30; 

	private static Robot robot = null;  
	
	private Robot() {
		this.reset(); 
	}
	
	public static synchronized Robot getInstance() {
		if (robot == null) {
			robot = new Robot(); 
		}
		return robot; 
	}
	
	public void reset() {
		Consts.SONAR_S.getDistance(); 
		Consts.NOTE_MOTOR.resetTachoCount(); 
		Consts.PITCH_MOTOR.resetTachoCount(); 
	}

	private int getNote() {
		int angle = (-Consts.NOTE_MOTOR.getTachoCount() + NOTE_ROTATION_OFFSET) % 360; 
		if (angle < 0) {
			angle += 360; 
		}
		int note = angle / NOTE_RANGE; 
		if (note > 6) {
			note = 4; 
		}
		LCD.drawString("Note:" + (note + 1), 0, 2); 
		return note; 
	}
	
	private int getPitch() {
		int angle = (Consts.PITCH_MOTOR.getTachoCount() + PITCH_ROTATION_OFFSET); 
		if (angle < 0) {
			angle = 0; 
		}
		int pitch = angle / PITCH_RANGE; 
		if (pitch >= MusicConsts.NOTE_FREQ.length) {
			pitch = MusicConsts.NOTE_FREQ.length - 1; 
		}
		LCD.drawString("Pitch:" + (pitch + MusicConsts.BASE_PITCH), 0, 3); 
		return pitch; 
	}
	
	private boolean isRisingTone() {
		int d = Consts.SONAR_S.getDistance(); 
		boolean r = d < RISING_VAL; 
		LCD.drawString(new StringBuffer("Rising:").append(r).append(", ").append(d).append("   ").toString(), 0, 5); 
		return r; 
	}
	
	public int getVol() {
		boolean pressed = Consts.TOUCH_S.isPressed(); 
		int vol = pressed ? Sound.VOL_MAX : 0; 
		LCD.drawString("Vol:" + vol + "   ", 0, 4); 
		return vol; 
	}
	
	public int getFreq() {
		int pitch = this.getPitch(); 
		int note = this.getNote(); 
		int[] freqs = MusicConsts.NOTE_FREQ[pitch][note]; 
		return freqs[(freqs.length > 1 && this.isRisingTone()) ? 1 : 0]; 
	}
	
	public void detectAndPlay() {
		final int freq = robot.getFreq(); 
		final int dur = MusicConsts.MIN_DURATION; 
		final int vol = robot.getVol(); 
		Sound.setVolume(vol); 
		Sound.playTone(freq, dur); 
	}
}
