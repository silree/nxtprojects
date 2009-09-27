package org.programus.nxj.alpharex.bypass.behaviors;

import lejos.robotics.subsumption.Behavior;

import org.programus.nxj.alpharex.bypass.utils.Robot;
import org.programus.nxj.alpharex.bypass.utils.RobotIntention;

public class WalkBehavior implements Behavior {

	@Override
	public void action() {
		Robot.L_LEG_MOTOR.setSpeed(Robot.WALK_SPEED); 
		Robot.R_LEG_MOTOR.setSpeed(Robot.WALK_SPEED); 
		Robot.L_LEG_MOTOR.forward(); 
		Robot.R_LEG_MOTOR.forward(); 
	}

	@Override
	public void suppress() {
		Robot.L_LEG_MOTOR.stop(); 
		Robot.R_LEG_MOTOR.stop();
	}

	@Override
	public boolean takeControl() {
		return RobotIntention.getInstance().isWalk();
	}

}
