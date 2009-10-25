package org.programus.nxj.rockboy.games.bball;

import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.TextMenu;

import org.programus.nxj.rockboy.games.bball.ctrls.BBGame;
import org.programus.nxj.util.DisplayUtil;

public class BBall {
	public static void main(String[] args) {
//		RConsole.openUSB(15000); 
		BBGame game = new BBGame(); 
		game.initialize(); 
		try {
			int winRow = 3; 
			TextMenu againMenu = new TextMenu(new String[]{"YES", "NO"}, 6, "Play Again?"); 
			do {
				if (game.play()) {
					LCD.clear(); 
					DisplayUtil.drawStringCenter("YOU WIN!", winRow); 
					LCD.refresh(); 
				} else {
					break; 
				}
			} while (againMenu.select() == 0); 
		} catch (IOException e) {
			Sound.buzz(); 
			LCD.clear(); 
			LCD.drawString("Level File Error:", 0, 2); 
			LCD.drawString(e.getMessage(), 0, 3); 
			LCD.refresh(); 
		}
//		Button.ESCAPE.waitForPressAndRelease(); 
//		RConsole.close(); 
	}
}