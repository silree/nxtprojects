package org.programus.nxj.alpharex.bypass.behaviors;

import lejos.nxt.LCD;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;

import org.programus.nxj.alpharex.bypass.utils.DistanceMonitor;
import org.programus.nxj.alpharex.bypass.utils.RobotIntention;

public class StatusDisplayBehavior implements Behavior {
	
	String[] intentions = {
			"Walk", 
			"Turn Left", 
			"Turn Right", 
			}; 

	@Override
	public void action() {
		LCD.drawString("I want to " + intentions[RobotIntention.getInstance().getDirection()], 0, 3); 
		LCD.drawString("Distance: " + DistanceMonitor.getInstance().getLatestDistance(), 0, 4); 
		RConsole.println("Distance: " + DistanceMonitor.getInstance().getLatestDistance()); 
	}

	@Override
	public void suppress() {
		// nothing
	}

	@Override
	public boolean takeControl() {
		return true;
	}

}
