package org.programus.nxj.rockboy.games.bball.ctrls;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.microedition.lcdui.Graphics;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;
import lejos.util.TextMenu;

import org.programus.nxj.rockboy.core.Game;
import org.programus.nxj.rockboy.core.World;
import org.programus.nxj.util.Condition;
import org.programus.nxj.util.DisplayUtil;
import org.programus.nxj.util.SoundUtil;
import org.programus.nxj.util.TimeUtil;

/**
 * A BBGame object represent one round game of the BBall game. 
 * @author Programus
 *
 */
public class BBGame extends Game {
	public final static int TIME_MODE = 0; 
	public final static int SCORE_MODE = 1; 
	
	private final static String[] GAME_MODES = {
		"TIME MODE", "SCORE MODE", 
	};
	
	private int gameMode; 
	private long totalValue; 
	
	private final static String profileName = ".bball"; 
	private static long bestScore = -1; 
	private static long bestTime; 
	private static Graphics g; 
	
	private LevelFactory levelFactory; 
	
	static {
		File profile = new File(profileName); 
		if (profile.exists() && profile.canRead()) {
			try {
				DataInputStream in = new DataInputStream(new FileInputStream(profile));
				bestTime = in.readLong(); 
				bestScore = in.readLong(); 
				in.close(); 
			} catch (IOException e) {
			}
		}
		if (bestTime <= 0) {
			bestTime = Long.MAX_VALUE; 
		}
		g = new Graphics(); 
	}
	
	public BBGame() {
		this.levelFactory = new LevelFactory(this); 
	}
	
	public void initialize() {
		World.initialize(); 
	}
	
	private void showStartScreen() {
		g.clear(); 
		g.fillRect(0, LCD.CELL_HEIGHT + (LCD.CELL_HEIGHT >> 1), g.getWidth(), LCD.CELL_HEIGHT << 1); 
		DisplayUtil.drawStringCenter("BBALL", LCD.CELL_HEIGHT << 1, true); 
	}
	
	public void promptMode(int row) {
		// Play a start music. 
		new Thread(new Runnable() {
			@Override
			public void run() {
				SoundUtil.playNote(Sound.PIANO, 5, 5, false, 300); 
				SoundUtil.playNote(Sound.PIANO, 5, 5, false, 100); 
				SoundUtil.playNote(Sound.PIANO, 5, 5, false, 100); 
				SoundUtil.playNote(Sound.PIANO, 5, 5, false, 150); 
				SoundUtil.playNote(Sound.PIANO, 5, 3, false, 150); 
				SoundUtil.playNote(Sound.PIANO, 5, 6, false, 150); 
				SoundUtil.playNote(Sound.PIANO, 5, 5, false, 500); 
			}
		}).start(); 
		
		this.showStartScreen(); 
		TextMenu modeMenu = new TextMenu(GAME_MODES, row, "SELECT A MODE:"); 
		this.gameMode = modeMenu.select(); 
		if (this.gameMode < 0) {
			quit(); 
		}
	}
	
	/**
	 * Save records into file. 
	 */
	public void saveRecords() {
		File profile = new File(profileName); 
		try {
			if (!profile.exists()) {
				profile.createNewFile(); 
			}
			DataOutputStream out = new DataOutputStream(new FileOutputStream(profile)); 
			out.writeLong(bestTime); 
			out.writeLong(bestScore); 
			out.close();
		} catch (IOException e) {
			Sound.buzz(); 
			System.out.println(e.getMessage()); 
		} 
	}
	
	/**
	 * Refresh the record. 
	 * @return true if old record is broken
	 */
	private boolean refreshRecords() {
		boolean newRecord = false; 
		if (this.isScoreMode() && this.totalValue > bestScore) {
			bestScore = this.totalValue; 
			newRecord = true; 
		}
		if (this.isTimeMode() && this.totalValue < bestTime) {
			bestTime = this.totalValue; 
			newRecord = true;
		}
		
		return newRecord; 
	}
	
	/**
	 * System time (long) -> HH:MM:SS.ms
	 * @param time
	 * @return
	 */
	private String getTimeString(long time) {
		int[] hms = TimeUtil.getHMSms(time); 
		StringBuffer sb = new StringBuffer(); 
		sb.append(hms[0]).append(':'); 
		if (hms[1] < 10) {
			sb.append('0'); 
		}
		sb.append(hms[1]).append(':'); 
		if (hms[2] < 10) {
			sb.append('0'); 
		}
		sb.append(hms[2]).append('.'); 
		if (hms[3] < 100) {
			sb.append('0'); 
		}
		sb.append(hms[3] / 10); 
		
		return sb.toString(); 
	}
	
	/**
	 * Show the score at the end of the level. 
	 * @param level
	 */
	private void showValue(GameLevel level) {
		DisplayUtil.drawStringCenter(level.getTitle(), 1, true); 
		int startY = (LCD.SCREEN_HEIGHT >> 1) - LCD.CELL_HEIGHT; 
		if (this.isScoreMode()) {
			LCD.drawString("SCORE:", 0, startY, true); 
			DisplayUtil.drawStringRight(String.valueOf(level.getGameValue()), startY, 0, true); 
			startY += LCD.CELL_HEIGHT; 
			LCD.drawString("TOTAL:", 0, startY, true); 
			DisplayUtil.drawStringRight(String.valueOf(this.totalValue), startY, 0, true); 
		}
		if (this.isTimeMode()) {
			LCD.drawString("TIME:", 0, startY, true); 
			DisplayUtil.drawStringRight(this.getTimeString(level.getGameValue()), startY, 0, true); 
			startY += LCD.CELL_HEIGHT; 
			LCD.drawString("TOTAL:", 0, startY, true); 
			DisplayUtil.drawStringRight(this.getTimeString(this.totalValue), startY, 0, true); 
		}
		LCD.refresh(); 
	}
	
	private void showNewRecord() {
		// play a music for breaking a record. 
		new Thread(new Runnable() {
			@Override
			public void run() {
				int duration = 100; 
				SoundUtil.playNote(5, 1, false, duration, true); 
				SoundUtil.playNote(5, 2, false, duration, true); 
				SoundUtil.playNote(5, 3, false, duration, true); 
				duration = 200; 
				SoundUtil.playNote(5, 5, false, duration, true); 
				duration = 100; 
				SoundUtil.playNote(5, 3, false, duration, true); 
				duration = 500; 
				SoundUtil.playNote(5, 5, false, duration, true); 
			}
		}).start(); 
		int row = 3; 
		g.clear(); 
		g.fillRect(0, row * LCD.CELL_HEIGHT - 1, g.getWidth(), LCD.CELL_HEIGHT + 1); 
		DisplayUtil.drawStringCenter("NEW RECORD!", row * LCD.CELL_HEIGHT, true); 
		row++; 
		if (this.isScoreMode()) {
			DisplayUtil.drawStringCenter(String.valueOf(this.totalValue), row); 
		}
		if (this.isTimeMode()) {
			DisplayUtil.drawStringCenter(this.getTimeString(this.totalValue), row); 
		}
		g.refresh(); 
	}
	
	private void showBestRecord() {
		g.clear(); 
		String prompt; 
		if (this.isScoreMode()) {
			int y = LCD.CELL_HEIGHT; 
			prompt = "BEST SCORE:"; 
			g.fillRect(0, y - 1, prompt.length() * LCD.CELL_WIDTH + 1, LCD.CELL_HEIGHT + 1); 
			g.drawString(prompt, 1, y, true); 
			y += LCD.CELL_HEIGHT; 
			DisplayUtil.drawStringRight(String.valueOf(bestScore), y, 0, false); 
			y += LCD.CELL_HEIGHT; 
			prompt = "YOUR SCORE:"; 
			g.fillRect(0, y - 1, prompt.length() * LCD.CELL_WIDTH + 1, LCD.CELL_HEIGHT + 1); 
			g.drawString(prompt, 1, y, true); 
			y += LCD.CELL_HEIGHT; 
			DisplayUtil.drawStringRight(String.valueOf(this.totalValue), y, 0, false); 
		}
		if (this.isTimeMode()) {
			int y = LCD.CELL_HEIGHT; 
			prompt = "BEST TIME:"; 
			g.fillRect(0, y - 1, prompt.length() * LCD.CELL_WIDTH + 1, LCD.CELL_HEIGHT + 1); 
			g.drawString(prompt, 1, y, true); 
			y += LCD.CELL_HEIGHT; 
			DisplayUtil.drawStringRight(this.getTimeString(bestTime), y, 0, false); 
			y += LCD.CELL_HEIGHT; 
			prompt = "YOUR TIME:"; 
			g.fillRect(0, y - 1, prompt.length() * LCD.CELL_WIDTH + 1, LCD.CELL_HEIGHT + 1); 
			g.drawString(prompt, 1, y, true); 
			y += LCD.CELL_HEIGHT; 
			DisplayUtil.drawStringRight(this.getTimeString(this.totalValue), y, 0, false); 
		}
		g.refresh(); 
	}
	
	private void reset() {
		this.promptMode(6); 
		this.totalValue = 0; 
		this.levelFactory.reset(); 
	}
	
	/**
	 * Play this game. 
	 * @return true - normally exit; false - user canceled. 
	 */
	public boolean play() throws IOException {
		this.reset(); 
		boolean gameStopped = false; 
		Condition stopCondition = GameLevel.DEFAULT_STOP_CONDITION; 
		Condition touchPauseCondition = new TouchSwitchPauseCondition(); 
		Condition buttonPauseCondition = new ButtonSwitchPauseCondition(Button.ENTER); 
		MultiOrCondition pauseCondition = new MultiOrCondition(); 
		pauseCondition.addCondition(touchPauseCondition); 
		pauseCondition.addCondition(buttonPauseCondition); 
		for (GameLevel level = this.levelFactory.getNextLevel(); level != null; level = levelFactory.getNextLevel()) {
			if (!level.play(stopCondition, pauseCondition)) {
				gameStopped = true; 
				break; 
			}
			
			this.totalValue += level.getGameValue(); 
			this.showValue(level); 
			final int SCORE_TIME = 3000; 
			for (long t = System.currentTimeMillis(); !stopCondition.isSatisfied() && System.currentTimeMillis() < SCORE_TIME + t;) {
				Delay.msDelay(20); 
			}
		}
		
		if (!gameStopped) {
			if (this.refreshRecords()) {
				this.showNewRecord(); 
				this.saveRecords(); 
			} else {
				this.showBestRecord(); 
			}
			final int scoreTime = 3000; 
			for (long t = System.currentTimeMillis(); !stopCondition.isSatisfied() && System.currentTimeMillis() < scoreTime + t;) {
				Delay.msDelay(20); 
			}
		}
		return !gameStopped; 
	}
	
	public boolean isTimeMode() {
		return this.gameMode == TIME_MODE; 
	}
	
	public boolean isScoreMode() {
		return this.gameMode == SCORE_MODE;
	}
	
}
