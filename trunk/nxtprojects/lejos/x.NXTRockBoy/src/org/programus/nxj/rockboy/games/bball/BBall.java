package org.programus.nxj.rockboy.games.bball;

import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;

import org.programus.nxj.rockboy.core.World;
import org.programus.nxj.rockboy.games.bball.ctrls.GameLevel;
import org.programus.nxj.rockboy.games.bball.ctrls.LevelFactory;

public class BBall {
	public static void main(String[] args) {
//		RConsole.openUSB(15000); 
		World.initialize(); 
		LevelFactory levelFactory = new LevelFactory(); 
		boolean gameStopped = false; 
		try {
			for (GameLevel level = levelFactory.getNextLevel(); level != null; level = levelFactory.getNextLevel()) {
				if (!level.play(null, null)) {
					gameStopped = true; 
					break; 
				}
			}
		} catch (IOException e) {
			Sound.buzz(); 
			LCD.clear(); 
			LCD.drawString("Level File Error:", 0, 2); 
			LCD.drawString(e.getMessage(), 0, 3); 
			LCD.refresh(); 
		}
		
		if (!gameStopped) {
			LCD.clear(); 
			LCD.drawString("YOU WIN!", 4, 3); 
			LCD.refresh(); 
		}
		
		Button.ESCAPE.waitForPressAndRelease(); 
//		RConsole.close(); 
	}
}
