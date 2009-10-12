package org.programus.nxj.rockboy.games.dropball;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;

public class DropBall {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Motor.A.resetTachoCount(); 
		Motor.A.setBrakePower(0); 
		Motor.A.setPower(0); 
		while (!Button.ESCAPE.isPressed()) {
			LCD.drawString("Angle: " + Motor.A.getTachoCount() + "   ", 0, 3); 
		}
		Button.ESCAPE.waitForPressAndRelease(); 
	}
}
