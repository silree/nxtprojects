package org.programus.nxj.rockboy.games.dropball;

import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.TextMenu;

import org.programus.nxj.rockboy.games.dropball.ctrls.DropBallGame;
import org.programus.nxj.util.DisplayUtil;
import org.programus.nxj.util.music.BGMBox;
import org.programus.nxj.util.music.Music;
import org.programus.nxj.util.music.SheetParam;

public class DropBall {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		RConsole.openUSB(15000); 
		DropBallGame game = new DropBallGame(); 
		game.initialize(); 
		initMusics(); 
		try {
			int overRow = 3; 
			TextMenu againMenu = new TextMenu(new String[]{"YES", "NO"}, 6, "Play Again?"); 
			do {
				// play game again and again until the player select no in play again menu. 
				if (game.play()) {
					LCD.clear(); 
					DisplayUtil.drawStringCenter("GAME OVER", overRow); 
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
	
	private static Music[] musicSheets = {
		new Music(new SheetParam(1200, 0), new String[]
		{
			"6c#/8 5b/8 6c#/8/16 60/16 5f#/4/8 50/4 6d/8 6c#/8 6d/4 6c#/4 5b/4/8 50/4 ",
			"6d/8 6c#/8 6d/4 5f#/4/8 50/4 5b/16 5a/16 5b/8 5a/8 5g#/8 5b/8 5a/4/8 ",
			"6c#/8 5b/8 6c#/8/16 60/16 5f#/4/8 50/4 6d/8 6c#/8 6d/4 6c#/4 5b/4/8 50/4 ",
			"6d/8 6c#/8 6d/4 5f#/4/8 50/4 5b/16 5a/16 5b/8 5a/8 5g#/8 5b/8 5a/4/8 ",
			"6c#/8 5b/8 6c#/8/16 60/16 5f#/4/8 50/4 6d/8 6c#/8 6d/4 6c#/4 5b/4/8 50/4 ",
			"6d/8 6c#/8 6d/4 5f#/4/8 50/4 5b/16 5a/16 5b/8 5a/8 5g#/8 5b/8 5a/4/8 ",
			"6c#/8 5b/8 6c#/8/16 60/16 5f#/4/8 50/4 6d/8 6c#/8 6d/4 6c#/4 5b/4/8 50/4 ",
			"6d/8 6c#/8 6d/4 5f#/4/8 50/4 5b/16 5a/16 5b/8 5a/8 5g#/8 5b/8 5a/4/8 ",
			"5g#/8 5a/16 5b/4/8 5a/16 5b/16 ", 
			"6c#/8 5b/8 5a/8 5g#/8 5f#/4 6d/4 ", 
			"6c#/4/8/32 60/32 6c#/8/16 6d/8/16 6c#/16 5a/16 6c#/16 6c#/2/4/8 30/2/8/16", 
		}), 
		
	}; 
	
	private static void initMusics() {
		BGMBox box = BGMBox.getInstance(); 
		box.setVol(50); 
		for (Music music : musicSheets) {
			box.addMusic(music); 
		}
	}
}
