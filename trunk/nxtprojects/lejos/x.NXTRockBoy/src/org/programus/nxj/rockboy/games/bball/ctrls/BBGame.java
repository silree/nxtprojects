package org.programus.nxj.rockboy.games.bball.ctrls;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;
import lejos.util.TextMenu;

import org.programus.nxj.rockboy.core.World;
import org.programus.nxj.util.Condition;
import org.programus.nxj.util.DisplayUtil;
import org.programus.nxj.util.TimeUtil;

public class BBGame {
	public final static int TIME_MODE = 0; 
	public final static int SCORE_MODE = 1; 
	
	private final static String[] GAME_MODES = {
		"TIME MODE", "SCORE MODE", 
	};
	
	private int gameMode; 
	private long totalValue; 
	
	private final static String profileName = ".bball"; 
	private static long bestScore; 
	private static long bestTime; 
	
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
	}
	
	public BBGame() {
		this.levelFactory = new LevelFactory(this); 
	}
	
	public void initialize() {
		World.initialize(); 
	}
	
	public void promptMode(int row) {
		TextMenu modeMenu = new TextMenu(GAME_MODES, row, "SELECT A MODE:"); 
		this.gameMode = modeMenu.select(); 
	}
	
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
		new Thread(new Runnable() {
			@Override
			public void run() {
				int duration = 100; 
				Sound.playTone(523, duration); 
				Delay.msDelay(duration); 
				Sound.playTone(587, duration);
				Delay.msDelay(duration); 
				Sound.playTone(659, duration); 
				Delay.msDelay(duration); 
				duration = 200; 
				Sound.playTone(784, duration); 
				Delay.msDelay(duration); 
				duration = 100; 
				Sound.playTone(659, duration); 
				Delay.msDelay(duration); 
				duration = 500; 
				Sound.playTone(784, duration); 
				Delay.msDelay(duration); 
			}
		}).start(); 
		int row = 3; 
		DisplayUtil.drawStringCenter("NEW RECORD!", row); 
		row++; 
		if (this.isScoreMode()) {
			DisplayUtil.drawStringCenter(String.valueOf(this.totalValue), row); 
		}
		if (this.isTimeMode()) {
			DisplayUtil.drawStringCenter(this.getTimeString(this.totalValue), row); 
		}
		LCD.refresh(); 
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
		for (GameLevel level = this.levelFactory.getNextLevel(); level != null; level = levelFactory.getNextLevel()) {
			if (!level.play(stopCondition, null)) {
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
		
		if (this.refreshRecords()) {
			this.showNewRecord(); 
			this.saveRecords(); 
			final int CONGRATULATION_TIME = 3000; 
			for (long t = System.currentTimeMillis(); !stopCondition.isSatisfied() && System.currentTimeMillis() < CONGRATULATION_TIME + t;) {
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
	
	public static void main(String[] args) {
		BBGame game = new BBGame(); 
		System.out.println(game.getTimeString(System.currentTimeMillis())); 
	}
}
