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
	
	private static Music[] musics = {
		// Final Countdown
		new Music(new SheetParam(1200, 0, new int[]{20, 0, 10, 0}, false), new String[] {
			"60/4/8 6c#/8 5b/8 6c#/8/16 60/16 5f#/4/8 50/4 6d/8 6c#/8 6d/4 6c#/4 5b/4/8 50/4 ",
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
		
		// Smooth Criminal
		new Music(new SheetParam(SheetParam.SLOW_BEAT_DUR, 1, new int[] {10, 0, 20, 0}, false), new String[] {
			"3A/8 3A/16 3A/16 3G/16 3A/16 3B/4 30/8 3A/16 3B/16 4C/8 4C/8 30/8 3B/16 4C/16 3B/16 3B/16 3G/4 3A/4 ", 
			"3A/8 3A/32 3A/16 3G/16 3A/16 3B/4 30/8 3A/16 3B/16 4C/8 4C/8 30/8 3B/16 4C/16 3B/16 3B/16 3G/4 3A/4 ", 
			"3A/8 3A/32 3A/16 3G/16 3A/16 3B/4 30/8 3A/16 3B/16 4C/8 4C/8 30/8 3B/16 4C/16 3B/8 3G/4 3A/4 ", 
			"3A/8 3A/32 3A/16 3G/16 3A/16 3B/4 30/8 3A/16 3B/16 4C/4 40/8 3B/16 4C/16 3B/8 3G/4 3A/4/8 ", 
			"30/8 ", 
			"3A/16 3A/16 3A/16 4E/16 4D/8 3A/4 3A/8 3A/16 3A/16 3A/16 4E/16 4D/8 3A/4 3A/16 3A/16 3B/8 3G/8 3G/16 3A/8 ", 
			"3A/16 3A/16 3A/16 4E/16 4D/8 3A/4 3A/8 3A/16 3A/16 3A/16 4E/16 4D/8 3A/4 3A/16 3A/16 3B/8 3G/8 3G/16 3B/8 ", 
			"3A/16 3A/16 3A/16 4E/16 4D/8 3A/4 3A/8 3A/16 3A/16 3A/16 4E/16 4D/8 3A/4 3A/16 3A/16 3B/8 3G/8 3G/16 3A/8 ", 
			"3A/16 3A/16 3A/16 4E/16 4D/8 3A/4 4E/8 4E/16 4E/16 4E/16 4E/16 4G/8 4E/4 4F/16 4E/16 4G/8 4E/8 4G/8 4A/4 ", 
			"40/8 ", 
			"5C/32 5C/16 5C/16 5C/16 4B/8 4G/8 40/8 4A/16 4B/16 4A/8 4F/8 40/8 4A/16 4A/16 4G/8 4E/4 ", 
			"40/8 ", 
			"5C/32 5C/16 5C/8 5C/16 5C/16 4B/8 4G/8 40/8 5C/16 5D/16 4A/8 4F/8 40/8 4A/16 4A/16 4G#/8 4G#/8 4B/8 5C/8 ", 
			"40/8 ", 
			"5C/16 5C/16 5C/16 5C/16 5C/32 5C/16 4B/8 4G/8 4A/16 4A/16 4B/16 4A/8 4F/8 40/8 4A/16 4A/16 4B/8 4G/4 ", 
			"40/8 ", 
			"5C/16 5C/16 5C/16 5C/16 5C/16 5C/16 4B/8 4G/8 40/8 4A/16 4B/16 4A/8 4F/8 40/8 4A/16 4A/16 4B/8 4G#/4 ", 
			"40/8 ", 
			"3A/16 3A/16 3A/16 4E/16 4D/8 3A/4 3A/8 3A/16 3A/16 3A/16 4E/16 4D/8 3A/4 3A/16 3A/16 3B/8 3G/8 3G/16 3A/8 ", 
			"3A/16 3A/16 3A/16 4E/16 4D/8 3A/4 3A/8 3A/16 3A/16 3A/16 4E/16 4D/8 3A/4 3A/16 3A/16 3B/8 3G/8 3G/16 3A/8 ", 
			"3A/16 3A/16 3A/16 4E/16 4D/8 3A/4 3A/8 3A/16 3A/16 3A/16 4E/16 4D/8 3A/4 3A/16 3A/16 3B/8 3G/8 3G/16 3A/8 ", 
			"4E/16/32 40/32 4C/16/32 40/32 4D/16/32 40/32 4D/8 ", 
			"40/4", 
			"4E/16/32 40/32 4C/16/32 40/32 4D/16/32 40/32 4D/8 ", 
			"4C/8 4E/8 40/16 4D/16 4C/16 3A/8 40/8", 
		}), 
	}; 
	
	private static void initMusics() {
		BGMBox box = BGMBox.getInstance(); 
		box.setVol(50); 
		for (Music music : musics) {
			box.addMusic(music); 
		}
	}
}
