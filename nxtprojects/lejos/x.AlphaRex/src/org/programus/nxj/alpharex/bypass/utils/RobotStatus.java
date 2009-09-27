package org.programus.nxj.alpharex.bypass.utils;

import java.util.BitSet;

public class RobotStatus implements Runnable {
	public static final int LEGS_READY = 0;
	public static final int WALKING = 1;
	public static final int TURNING = 2;
	public static final int LEFT = 3;
	
	private static final int LEGS_MAX_SUB = 10; 
	
	private static Thread updateThread = null; 
	
	private static RobotStatus rs = new RobotStatus(); 
	private BitSet status = new BitSet(4);
	
	private boolean running = false; 
	
	public static RobotStatus getInstance() {
		return rs; 
	}
	
	public static void setAutoUpdateStatus(boolean auto) {
		rs.running = auto; 
		if (updateThread == null) {
			updateThread = new Thread("autoupdate", rs); 
			updateThread.setDaemon(true); 
		}
		if (auto) {
			updateThread.start(); 
		}
	}
	
	public boolean isStatusTrue(int status) {
		synchronized(this.status) {
			return this.status.get(status); 
		}
	}
	
	public void setStatus(int status, boolean val) {
		synchronized(this.status) {
			if (val) {
				this.status.set(status); 
			} else {
				this.status.clear(status); 
			}
		}
	}
	
	@Override
	public void run() {
		while (running) {
			boolean isLMoving = Robot.L_LEG_MOTOR.isMoving(); 
			boolean isRMoving = Robot.R_LEG_MOTOR.isMoving(); 
			int sub = Math.abs((Robot.R_LEG_MOTOR.getTachoCount() - Robot.L_LEG_MOTOR.getTachoCount()) % 360); 
			this.setStatus(LEGS_READY, sub < LEGS_MAX_SUB); 
			this.setStatus(WALKING, isLMoving && isRMoving); 
			this.setStatus(TURNING, (isLMoving || isRMoving) && !(isLMoving && isRMoving)); 
			this.setStatus(LEFT, isLMoving); 
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {} 
		}
	}
}
