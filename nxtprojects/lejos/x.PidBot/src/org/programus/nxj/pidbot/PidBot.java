package org.programus.nxj.pidbot;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.RConsole;

import org.programus.nxj.pidbot.pid.PidController;
import org.programus.nxj.pidbot.util.Consts;
import org.programus.nxj.pidbot.util.SoundPlayer;

public class PidBot {

	public static void main(String[] args) {
//		RConsole.openUSB(15000); 
		PidController pidCtrl = new PidController(); 
		pidCtrl.init(); 
		SoundPlayer player = SoundPlayer.getInstance(); 
		while(!Button.ESCAPE.isPressed()) {
			int pid = pidCtrl.getPid(); 
			RConsole.println("PID: " + pid); 
			int speed = Math.abs(pid * Consts.SCALE); 
			if (speed > 900) {
				speed = 900; 
			}
			RConsole.println("Speed: " + speed); 
			Consts.MOTOR.setSpeed(speed); 
			if (pid == 0) {
				Consts.MOTOR.stop(); 
			} else if (pid > 0) {
				player.playPursue(); 
				LCD.clear(); 
				LCD.drawString("Pursuing...", 0, 3); 
				Consts.MOTOR.backward(); 
			} else if (pid < 0) {
				player.playRunaway(); 
				LCD.clear(); 
				LCD.drawString("Running away...", 0, 3); 
				Consts.MOTOR.forward(); 
			}
		}
		Consts.MOTOR.stop(); 
		Button.ESCAPE.waitForPressAndRelease(); 
		RConsole.close(); 
	}

}
