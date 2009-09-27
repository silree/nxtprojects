package org.programus.nxj.alpharex.bypass;

import org.programus.nxj.alpharex.bypass.utils.Robot;


public class PrepareRobot {

	public static void main(String[] args) {
		Robot.L_LEG_MOTOR.setSpeed(Robot.WALK_SPEED); 
		Robot.L_LEG_MOTOR.regulateSpeed(true); 
		Robot.L_LEG_MOTOR.forward(); 
		while(!Robot.L_TOUCH.isPressed()); 
		while(Robot.L_TOUCH.isPressed()); 
		Robot.L_LEG_MOTOR.stop(); 
		Robot.L_LEG_MOTOR.resetTachoCount(); 
		Robot.L_LEG_MOTOR.rotate(-Robot.OFFSET_ANGLE); 

		Robot.R_LEG_MOTOR.setSpeed(Robot.WALK_SPEED); 
		Robot.R_LEG_MOTOR.regulateSpeed(true); 
		Robot.R_LEG_MOTOR.forward(); 
		while(!Robot.R_TOUCH.isPressed()); 
		while(Robot.R_TOUCH.isPressed()); 
		Robot.R_LEG_MOTOR.stop(); 
		Robot.R_LEG_MOTOR.resetTachoCount(); 
		Robot.R_LEG_MOTOR.rotate(Robot.OFFSET_ANGLE); 
	}
}
