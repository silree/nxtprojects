package org.programus.nxj.rockboy.pc.wii;

import org.programus.nxj.rockboy.pc.PropMan;

public class WiimoteData {
	private double angle;
	private double angleOffset = Double.parseDouble(PropMan.getInstance().getProperty(PropMan.WII_ANGLEOFFSET)); 
	
	
	private static WiimoteData wd = new WiimoteData();
	
	private WiimoteData() {}
	
	public static WiimoteData getInstance() {
		return wd; 
	}

	/**
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}
	
	public double getAngle4NXT() {
		return angle - angleOffset; 
	}

	/**
	 * @param angle the angle to set
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}; 	
}
