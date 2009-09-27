package org.programus.nxj.alpharex.bypass.behaviors;

import lejos.robotics.subsumption.Behavior;

import org.programus.nxj.alpharex.bypass.utils.DistanceMonitor;
import org.programus.nxj.alpharex.bypass.utils.Robot;
import org.programus.nxj.alpharex.bypass.utils.RobotIntention;

public class AbortTurnBehavior implements Behavior {
	
	private final static int INTERVAL = 100; 
	private final static int MAX_COUNT = 3; 
	
	private long lastTimestamp = 0; 
	private int count = 0; 

	@Override
	public void action() {
		Robot.L_LEG_MOTOR.stop(); 
		Robot.R_LEG_MOTOR.stop(); 
		int lTacho = Robot.L_LEG_MOTOR.getTachoCount(); 
		int rTacho = Robot.R_LEG_MOTOR.getTachoCount(); 
		Robot.R_LEG_MOTOR.rotate((rTacho - lTacho) % 360); 
		RobotIntention.getInstance().wantWalk(); 
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
			ret = RobotIntention.getInstance().isTurn() && monitor.getDistance() > Robot.NEAR;
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
