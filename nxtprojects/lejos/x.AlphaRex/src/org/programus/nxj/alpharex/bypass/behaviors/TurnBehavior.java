package org.programus.nxj.alpharex.bypass.behaviors;

import lejos.nxt.Motor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;

import org.programus.nxj.alpharex.bypass.utils.Robot;
import org.programus.nxj.alpharex.bypass.utils.RobotIntention;

public class TurnBehavior implements Behavior {

	@Override
	public void action() {
		this.turn(RobotIntention.getInstance().getDirection()); 
	}
	
	private void turn(int dir) {
		int offset = dir == Robot.LEFT ? Robot.OFFSET_ANGLE : -Robot.OFFSET_ANGLE; 
		Motor turnLeg = dir == Robot.LEFT ? Robot.L_LEG_MOTOR : Robot.R_LEG_MOTOR; 
		Motor stickLeg = dir == Robot.LEFT ? Robot.R_LEG_MOTOR : Robot.L_LEG_MOTOR; 
		
		int stickLegTacho = (stickLeg.getTachoCount() - offset) % 360; 
		while(Math.abs(stickLegTacho) > 5) {
			turnLeg.stop(); 
			RConsole.println("adjust stick leg - " + stickLegTacho); 
			if (stickLegTacho > 180) {
				stickLegTacho -= 360; 
			} else if (stickLegTacho < -180) {
				stickLegTacho += 360; 
			}
			RConsole.println("adjust stick leg real - " + stickLegTacho); 
			stickLeg.rotate(-stickLegTacho); 
			stickLeg.stop(); 
			stickLegTacho = (stickLeg.getTachoCount() - offset) % 360; 
		} ; 
		turnLeg.setSpeed(Robot.TURN_SPEED); 
		turnLeg.forward(); 
	}

	@Override
	public void suppress() {
		Robot.L_LEG_MOTOR.stop(); 
		Robot.R_LEG_MOTOR.stop();
	}

	@Override
	public boolean takeControl() {
		return RobotIntention.getInstance().isTurn();
	}

}
