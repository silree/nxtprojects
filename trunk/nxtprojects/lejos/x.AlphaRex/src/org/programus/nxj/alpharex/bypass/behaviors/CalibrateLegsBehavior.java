package org.programus.nxj.alpharex.bypass.behaviors;

import lejos.robotics.subsumption.Behavior;

import org.programus.nxj.alpharex.bypass.utils.Robot;
import org.programus.nxj.alpharex.bypass.utils.RobotStatus;

public class CalibrateLegsBehavior implements Behavior {
	
	private RobotStatus status = RobotStatus.getInstance(); 

	@Override
	public void action() {
		int lTacho = Robot.L_LEG_MOTOR.getTachoCount(); 
		int rTacho = Robot.R_LEG_MOTOR.getTachoCount(); 
		Robot.R_LEG_MOTOR.rotate((rTacho - lTacho) % 360); 
	}

	@Override
	public void suppress() {
		// nothing
	}

	@Override
	public boolean takeControl() {
		status = RobotStatus.getInstance(); 
		return status.isStatusTrue(RobotStatus.WALKING) && !status.isStatusTrue(RobotStatus.LEGS_READY);
	}

}
