package org.programus.nxj.music;

import lejos.nxt.Button;

import org.programus.nxj.music.robot.Robot;
import org.programus.nxj.music.utils.Initializer;

public class NXTMusic {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		RConsole.openUSB(15000); 
		Robot robot = Robot.getInstance(); 
		Initializer.getInstance().runInit(); 
		while(!Button.ESCAPE.isPressed()) {
			robot.detectAndPlay(); 
		}
		Button.ESCAPE.waitForPressAndRelease(); 
//		RConsole.close(); 
	}
}
