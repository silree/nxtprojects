package org.programus.nxj.alpharex.bypass.utils;


public class RobotIntention {
	private static RobotIntention ri = new RobotIntention(); 
	private int intention = Robot.STRAIGHT; 
	
	private RobotIntention() {
	}
	
	public static RobotIntention getInstance() {
		return ri; 
	}
	
	public boolean isWalk() {
		return this.intention == Robot.STRAIGHT; 
	}
	
	public boolean isTurn() {
		return this.intention != Robot.STRAIGHT; 
	}
	
	public int getDirection() {
		return this.intention; 
	}
	
	public synchronized void wantWalk() {
		this.intention = Robot.STRAIGHT; 
	}
	
	public synchronized void wantTurn(int direction) {
		this.intention = direction; 
	}
}
