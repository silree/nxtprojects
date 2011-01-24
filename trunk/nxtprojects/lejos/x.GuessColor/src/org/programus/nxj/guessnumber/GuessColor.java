package org.programus.nxj.guessnumber;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.TextMenu;

import org.programus.nxj.guessnumber.game.GuessGame;

public class GuessColor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GuessGame game = new GuessGame(); 
		TextMenu againMenu = new TextMenu(new String[]{"YES", "NO"}, 6, "Play Again?"); 
		do {
			// play game again and again until the player select no in play again menu. 
			game.playOneGame();
			Button.ENTER.waitForPressAndRelease(); 
			LCD.clear(); 
		} while (againMenu.select() == 0); 

	}
}
