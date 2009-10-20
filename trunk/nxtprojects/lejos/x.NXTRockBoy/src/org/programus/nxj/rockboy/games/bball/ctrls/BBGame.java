package org.programus.nxj.rockboy.games.bball.ctrls;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import lejos.nxt.Sound;
import lejos.util.TextMenu;

import org.programus.nxj.rockboy.core.World;

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
		this.promptMode(6); 
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
	
	private void reset() {
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
		for (GameLevel level = this.levelFactory.getNextLevel(); level != null; level = levelFactory.getNextLevel()) {
			if (!level.play(null, null)) {
				gameStopped = true; 
				break; 
			}
			
			this.totalValue += level.getGameValue(); 
		}
		
		if (this.refreshRecords()) {
			Sound.beepSequence(); 
			this.saveRecords(); 
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
