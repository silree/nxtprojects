/*
 * NXT Music
 * 	by Programus, 2009.09.30
 * ====================
 * Inputs:
 * 	Port 1 - Ultrasonic Sensor 
 * 	Port 2 - Touch Sensor
 * 	Port 3 - Color Sensor (LEGO)
 * 	Motor B - Pitch input
 * 	Motor C - Note input
 * Outputs:
 * 	NXT Sound output
 * --------------------
 * Function: 
 * 	NXT Music can be used to play musics. 
 * 
 * 	Use the Note input to input the note like C, B, etc. 
 * 	For notes like c#, B#, E#, etc., you can put something near the ultrasonic sensor 
 * 	(I use my head as the something). 
 * 	And adjust the pitch by pushing or pulling pitch motor. 
 * 	Once you pressed the touch sensor, the note will be played. 
 */
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
