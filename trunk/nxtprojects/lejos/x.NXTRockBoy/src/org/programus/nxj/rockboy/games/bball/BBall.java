package org.programus.nxj.rockboy.games.bball;

import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;

import org.programus.nxj.rockboy.games.bball.ctrls.BBGame;

public class BBall {
	public static void main(String[] args) {
//		RConsole.openUSB(15000); 
		BBGame game = new BBGame(); 
		game.initialize(); 
		try {
			if (game.play()) {
				LCD.clear(); 
				LCD.drawString("YOU WIN!", 4, 3); 
				LCD.refresh(); 
			}
		} catch (IOException e) {
			Sound.buzz(); 
			LCD.clear(); 
			LCD.drawString("Level File Error:", 0, 2); 
			LCD.drawString(e.getMessage(), 0, 3); 
			LCD.refresh(); 
		}
		Button.ESCAPE.waitForPressAndRelease(); 
//		RConsole.close(); 
	}
}
