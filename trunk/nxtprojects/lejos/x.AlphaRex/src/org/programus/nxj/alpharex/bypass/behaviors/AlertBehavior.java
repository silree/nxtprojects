package org.programus.nxj.alpharex.bypass.behaviors;

import java.io.File;

import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;
import lejos.robotics.Colors.Color;
import lejos.robotics.subsumption.Behavior;

import org.programus.nxj.alpharex.bypass.utils.DistanceMonitor;
import org.programus.nxj.alpharex.bypass.utils.Robot;
import org.programus.nxj.alpharex.bypass.utils.RobotIntention;

public class AlertBehavior implements Behavior {
	
	private final static int ALARM_DURATION = 600; 
	private final static int TONE_BLUE = 1865; 
	private final static int TONE_RED = 1319; 
	
	private final static int ALERT_TIME = 2; 
	
	private final static int INTERVAL = 100; 
	private final static int MAX_COUNT = 2; 
	
	private long lastTimestamp = 0; 
	private int count = 0; 

	@Override
	public void action() {
		Robot.L_LEG_MOTOR.stop(); 
		Robot.R_LEG_MOTOR.stop(); 
		for (int i = 0; i < ALERT_TIME; i++) {
			this.alert(); 
			if (DistanceMonitor.getInstance().getDistance() > Robot.NEAR) {
				Sound.playSample(new File(Robot.THANKS_FILE)); 
				RobotIntention.getInstance().wantWalk(); 
				return; 
			}
		}
		RobotIntention.getInstance().wantTurn(this.findDir()); 
		Sound.playSample(new File(Robot.GO_FILE)); 
	}
	
	private void alert() {
		try {
			Thread.sleep(Sound.playSample(new File(Robot.WAY_FILE)) + 200);
		} catch (InterruptedException e) {} 
		for (int i = 0; i < 3; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					synchronized(Robot.COLOR_S) {
						Robot.COLOR_S.setFloodlight(Color.BLUE); 
					}
				}
			}).start(); 
			Sound.playTone(TONE_BLUE, ALARM_DURATION); 
			try {
				Thread.sleep(ALARM_DURATION);
			} catch (InterruptedException e) {} 
			new Thread(new Runnable() {
				@Override
				public void run() {
					synchronized(Robot.COLOR_S) {
						Robot.COLOR_S.setFloodlight(Color.RED); 
					}
				}
			}).start(); 
			Sound.playTone(TONE_RED, ALARM_DURATION); 
			try {
				Thread.sleep(ALARM_DURATION);
			} catch (InterruptedException e) {} 
		}
		synchronized(Robot.COLOR_S) {
			Robot.COLOR_S.setFloodlight(false); 
		}
	}
	
	private int findDir() {
		Robot.HEAD_MOTOR.setSpeed(Robot.FIND_SPEED); 
		Robot.HEAD_MOTOR.rotate(Robot.HEAD_LOOP_ANGLE, true); 
		DistanceMonitor monitor = DistanceMonitor.getInstance(); 
		int[] distances = new int[2]; 
		monitor.setPause(true); 
		int delimit = Robot.HEAD_LOOP_ANGLE >> 1; 
		while(Robot.HEAD_MOTOR.isMoving()) {
			int distance = Robot.SONAR_S.getDistance(); 
			int angle = Robot.HEAD_MOTOR.getTachoCount(); 
			if (angle < delimit) {
				distances[0] += distance; 
			} else if (angle > delimit) {
				distances[1] += distance; 
			}
		}
		monitor.setPause(false); 
		return distances[0] > distances[1] ? Robot.LEFT : Robot.RIGHT; 
	}

	@Override
	public void suppress() {
		// nothing
	}

	@Override
	public boolean takeControl() {
		boolean ret = false; 
		long timestamp = System.currentTimeMillis(); 
		if (timestamp - this.lastTimestamp > INTERVAL) {
			// Walking and obstacle near
			DistanceMonitor monitor = DistanceMonitor.getInstance(); 
//			RConsole.println(monitor.toString()); 
			this.lastTimestamp = timestamp; 
			ret = RobotIntention.getInstance().isWalk() && monitor.getDistance() < Robot.NEAR;
			if (ret) {
				count ++; 
			} else {
				count = 0; 
			}
			if (count < MAX_COUNT) {
				ret = false; 
			} else {
				count = 0; 
			}
		}
		
		return ret; 
	}

}
