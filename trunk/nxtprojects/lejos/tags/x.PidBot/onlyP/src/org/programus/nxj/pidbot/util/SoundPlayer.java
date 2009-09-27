package org.programus.nxj.pidbot.util;

import java.io.File;

import lejos.nxt.Sound;

public class SoundPlayer {
	private static SoundPlayer player = new SoundPlayer(); 
	private SoundPlayer() {
	}
	
	public static SoundPlayer getInstance() {
		return player; 
	}
	
	private long pursuePlayEndTime = 0; 
	private long runawayPlayEndTime = 0; 
	
	public void playPursue() {
		long time = System.currentTimeMillis(); 
		if (time > this.pursuePlayEndTime) {
			int t = Sound.playSample(new File(Consts.PURSUE_FILE)); 
			this.pursuePlayEndTime = System.currentTimeMillis() + t; 
		}
	}
	
	public void playRunaway() {
		long time = System.currentTimeMillis(); 
		if (time > this.runawayPlayEndTime) {
			int t = Sound.playSample(new File(Consts.RUNAWAY_FILE)); 
			this.runawayPlayEndTime = System.currentTimeMillis() + t; 
		}
	}
}
