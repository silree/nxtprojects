package org.programus.nxj.pidbot.pid;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;

import org.programus.nxj.pidbot.util.Consts;
import org.programus.nxj.pidbot.util.Global;

public class PidController {
	
	private int error; 
	private int ei; 
	private int ed; 
	
	public void init() {
		if (Global.SONAR_SENSOR == null) {
			Global.SONAR_SENSOR = new UltrasonicSensor(Consts.SONAR_PORT); 
			Global.sleep(100); 
			RConsole.println("First distance:" + Global.SONAR_SENSOR.getDistance()); 
		}
		Sound.beep(); 
		String message = "Please enter orange button"; 
		RConsole.println(message); 
		LCD.drawString(message, 0, 3); 
		Button.ENTER.waitForPressAndRelease(); 
		message = "Detecting distance..."; 
		RConsole.println(message); 
		LCD.clear(); 
		LCD.drawString(message, 0, 3); 
		Sound.beep(); 
		Global.sleep(500); 
		Sound.beepSequenceUp(); 
		
		final int TIMES = 100; 
		int min = 0xff; 
		for (int i = 0; i < TIMES; i++) {
			int distance = Global.SONAR_SENSOR.getDistance(); 
			if (distance < min) {
				min = distance; 
			}
		}
		Global.traceDistance = min; 
		RConsole.println("trace distance=" + Global.traceDistance); 
		
		Sound.beepSequence(); 
	}
	
	public int getPid() {
		int prevError = this.error; 
		int distance = Global.SONAR_SENSOR.getDistance(); 
		if (distance >= 0xff) {
			return 0; 
		}
		RConsole.println("distance=" + distance); 
		this.error = distance - Global.traceDistance; 
		this.ei += this.error; 
		this.ed = this.error - prevError; 
		RConsole.println("p:" + this.error); 
		RConsole.println("i:" + this.ei); 
		RConsole.println("d:" + this.ed); 
		return Consts.P * this.error + Consts.I * this.ei + Consts.D * this.ed; 
	}
}
