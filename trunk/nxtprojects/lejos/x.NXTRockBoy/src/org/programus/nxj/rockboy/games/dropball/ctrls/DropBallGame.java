package org.programus.nxj.rockboy.games.dropball.ctrls;

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

import org.programus.nxj.rockboy.core.World;
import org.programus.nxj.rockboy.games.bball.ctrls.ButtonSwitchPauseCondition;
import org.programus.nxj.rockboy.games.bball.ctrls.KeyStopCondition;
import org.programus.nxj.rockboy.games.bball.ctrls.MultiOrCondition;
import org.programus.nxj.rockboy.games.bball.ctrls.TouchSwitchPauseCondition;
import org.programus.nxj.util.Condition;
import org.programus.nxj.util.DisplayUtil;
import org.programus.nxj.util.music.Music;
import org.programus.nxj.util.music.SheetParam;

/**
 * A DropBallGame object represent one round game of the Drop Ball game. 
 * @author Programus
 *
 */
public class DropBallGame {
	private final static String[] GAME_MODES = {
		"| MODE", "- MODE", 
	};
	
	/** Game mode: | or - */
	private int mode; 
	
	private long totalValue; 
	
	private final static String profileName = ".dball"; 
	private static long bestScore = -1; 
	private static Graphics g; 
	
	private GameLevel level; 
		
	static {
		File profile = new File(profileName); 
		if (profile.exists() && profile.canRead()) {
			try {
				DataInputStream in = new DataInputStream(new FileInputStream(profile));
				bestScore = in.readLong(); 
				in.close(); 
			} catch (IOException e) {
			}
		}
		g = new Graphics(); 
	}
	
	public DropBallGame() {
	}
	
	public void initialize() {
		World.initialize(); 
		this.level = new GameLevel(this); 
	}
	
	private void showStartScreen() {
		g.clear(); 
		g.fillRect(0, LCD.CELL_HEIGHT + (LCD.CELL_HEIGHT >> 1), g.getWidth(), LCD.CELL_HEIGHT << 1); 
		DisplayUtil.drawStringCenter("DROP BALL", LCD.CELL_HEIGHT << 1, true); 
	}
	
	public void promptMode(int row) {
		// Play a start music. 
		new Thread(new Runnable() {
			@Override
			public void run() {
				new Music(new SheetParam(1200, 0, new int[]{20, 0, 10, 0}, false), new String[] {
					"4c/4 4e/4 4g/4 5c/2", 
				}).play(Sound.PIANO); 
			}
		}).start(); 
		
		this.showStartScreen(); 
		TextMenu modeMenu = new TextMenu(GAME_MODES, row, "PLEASE SELECT:"); 
		this.mode = modeMenu.select(this.mode); 
		if (this.mode < 0) {
			System.exit(0); 
		}
	}
	
	public boolean isLightStepMode() {
		return this.mode == 0; 
	}
	
	public boolean isLightWidthMode() {
		return this.mode == 1; 
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
		if (this.totalValue > bestScore) {
			bestScore = this.totalValue; 
			newRecord = true;
		}
		
		return newRecord; 
	}
	
	private void showNewRecord() {
		// play a music for breaking a record. 
		new Thread(new Runnable() {
			@Override
			public void run() {
				new Music(new SheetParam(SheetParam.NORMAL_BEAT_DUR, 0, new int[]{20, 0, 10, 0}, false), new String[] {
					"50/8 5c/8/16 5d/16 5e/8 5g/4 5e/8 5g/2/8", 
				}).play(Sound.PIANO); 
			}
		}).start(); 
		int row = 3; 
		g.clear(); 
		g.fillRect(0, row * LCD.CELL_HEIGHT - 1, g.getWidth(), LCD.CELL_HEIGHT + 1); 
		DisplayUtil.drawStringCenter("NEW RECORD!", row * LCD.CELL_HEIGHT, true); 
		row++; 
		
		DisplayUtil.drawStringCenter(String.valueOf(this.totalValue), row); 
		
		g.refresh(); 
	}
	
	private void showBestRecord() {
		g.clear(); 
		String prompt; 
	
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
		
		g.refresh(); 
	}
	
	private void reset() {
		this.promptMode(6); 
		this.totalValue = 0; 
		this.level.initialize(); 
	}
	
	/**
	 * Play this game. 
	 * @return true - normally exit; false - user canceled. 
	 */
	public boolean play() throws IOException {
		this.reset(); 
		boolean gameStopped = false; 
		
		Condition stopCondition = new KeyStopCondition(Button.ESCAPE); 
		Condition touchPauseCondition = new TouchSwitchPauseCondition(); 
		Condition buttonPauseCondition = new ButtonSwitchPauseCondition(Button.ENTER); 
		MultiOrCondition pauseCondition = new MultiOrCondition(); 
		pauseCondition.addCondition(touchPauseCondition); 
		pauseCondition.addCondition(buttonPauseCondition); 
		gameStopped = !level.play(stopCondition, pauseCondition); 
		this.totalValue = level.getGameValue(); 
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
	
}
