package org.programus.nxj.music.utils;

import org.programus.nxj.music.robot.Robot;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;

public class Initializer {
	private static Initializer init = new Initializer(); 
	private Initializer() {
	}
	public static Initializer getInstance() {
		return init; 
	}
	
//	private int getSelectedInst() {
//		TextMenu instMenu = new TextMenu(MusicConsts.INST_NAMES, 1, "Please select a instrument:"); 
//		return instMenu.select(); 
//	}
//	
//	private void initRobot() {
//		Robot robot = Robot.getInstance(); 
//		robot.setInst(MusicConsts.INSTS[this.getSelectedInst()]); 
//	}
//	
	private void promptAdjustControllers() {
		Sound.beepSequence(); 
		LCD.clear(); 
		LCD.drawString("NXT Music", 4, 0); 
		LCD.drawString("Please reset ", 0, 3); 
		LCD.drawString("your ctrls", 3, 4); 
		LCD.drawString("Press Orange Key", 0, 5); 
		LCD.drawString("when ready", 3, 5); 
		Button.ENTER.waitForPressAndRelease(); 
		LCD.clear(); 
	}
	
	public void runInit() {
//		this.initRobot(); 
		this.promptAdjustControllers(); 
		Robot.getInstance().reset(); 
	}
}
