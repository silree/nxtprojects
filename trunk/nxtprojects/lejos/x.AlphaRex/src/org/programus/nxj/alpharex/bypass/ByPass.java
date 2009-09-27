package org.programus.nxj.alpharex.bypass;

import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

import org.programus.nxj.alpharex.bypass.behaviors.AbortTurnBehavior;
import org.programus.nxj.alpharex.bypass.behaviors.AlertBehavior;
import org.programus.nxj.alpharex.bypass.behaviors.TurnBehavior;
import org.programus.nxj.alpharex.bypass.behaviors.WalkBehavior;
import org.programus.nxj.alpharex.bypass.utils.DistanceMonitor;
import org.programus.nxj.alpharex.bypass.utils.Robot;
import org.programus.nxj.alpharex.bypass.utils.RobotStatus;

public class ByPass {
	private static void init() {
		RConsole.openBluetooth(15000); 
		Robot.L_LEG_MOTOR.regulateSpeed(true); 
		Robot.R_LEG_MOTOR.regulateSpeed(true); 
		Robot.HEAD_MOTOR.regulateSpeed(true); 
		RobotStatus.setAutoUpdateStatus(true); 
		DistanceMonitor.getInstance().startMonitoring(); 
	}
	
	public static void main(String[] args) {
		init(); 
		Behavior[] behaviors = {
//				new StatusDisplayBehavior(), 
				new WalkBehavior(), 
				new TurnBehavior(), 
//				new CalibrateLegsBehavior(), 
				new AlertBehavior(), 
				new AbortTurnBehavior(),
				}; 
		Arbitrator arbitrator = new Arbitrator(behaviors); 
		arbitrator.start(); 
	}
}
