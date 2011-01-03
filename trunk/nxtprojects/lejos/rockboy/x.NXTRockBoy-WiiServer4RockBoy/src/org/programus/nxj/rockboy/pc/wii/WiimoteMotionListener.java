package org.programus.nxj.rockboy.pc.wii;

import org.programus.nxj.rockboy.pc.ui.UIData;

import wiiusej.values.GForce;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;

public class WiimoteMotionListener extends WiimoteAdapter {
	@Override
	public void onMotionSensingEvent(MotionSensingEvent e) {
		super.onMotionSensingEvent(e);
		GForce g = e.getGforce(); 
		double x = g.getX(); 
		if (Math.abs(x) > 1) {
			x = x > 0 ? 1 : -1;
		}
		double angle = Math.toDegrees(Math.asin(x)); 
		if (g.getY() > 0) {
			angle = 180 - angle;
		}
		WiimoteData.getInstance().setAngle(angle); 
		UIData.getInstance().getFrame().repaint(); 
	}
}
