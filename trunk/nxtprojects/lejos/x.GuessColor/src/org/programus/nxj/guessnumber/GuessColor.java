package org.programus.nxj.guessnumber;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.TextMenu;

import org.programus.nxj.guessnumber.game.GuessGame;
import org.programus.nxj.util.DisplayUtil;
import org.programus.nxj.util.txtimg.TextImage;
import org.programus.nxj.util.txtimg.TextImage3x5;

public class GuessColor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Sound.setVolume(Sound.VOL_MAX); 
		displayDescription();
		LCD.refresh(); 
		Button.waitForPress(); 
		GuessGame game = new GuessGame(); 
		TextMenu againMenu = new TextMenu(new String[]{"YES", "NO"}, 6, "Play Again?"); 
		do {
			// play game again and again until the player select no in play again menu. 
			game.playOneGame();
			Button.ESCAPE.waitForPressAndRelease(); 
			LCD.clear(); 
		} while (againMenu.select() == 0); 
	}
	
	private static void displayDescription() {
		Graphics g = new Graphics(); 
		g.clear(); 
		g.fillRect(0, LCD.CELL_HEIGHT + (LCD.CELL_HEIGHT >> 1), g.getWidth(), LCD.CELL_HEIGHT << 1); 
		DisplayUtil.drawStringCenter("GUESS COLOR", LCD.CELL_HEIGHT << 1, true); 
		TextImage ti = new TextImage3x5(); 
		Image pressAnyKey = ti.getImage("PRESS ANY KEY"); 
		Image toStart = ti.getImage("TO START..."); 
		int y = LCD.SCREEN_HEIGHT - (LCD.CELL_HEIGHT << 1); 
		DisplayUtil.drawImageCenter(pressAnyKey, y, LCD.ROP_COPY); 
		y += LCD.CELL_HEIGHT; 
		DisplayUtil.drawImageCenter(toStart, y, LCD.ROP_COPY); 
	}
}
