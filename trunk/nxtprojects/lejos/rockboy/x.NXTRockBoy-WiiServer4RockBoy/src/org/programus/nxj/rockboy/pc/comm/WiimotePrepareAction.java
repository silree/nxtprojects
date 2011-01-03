package org.programus.nxj.rockboy.pc.comm;

import org.programus.nxj.rockboy.pc.PropMan;
import org.programus.nxj.rockboy.pc.wii.WiimoteConnector;
import org.programus.nxj.rockboy.pc.wii.WiimoteMotionListener;

import wiiusej.Wiimote;

public class WiimotePrepareAction implements Runnable {
	
	@Override
	public void run() {
		long timeout = Long.parseLong(PropMan.getInstance().getProperty(PropMan.WII_CONN_TIMEOUT)); 
		Wiimote wiimote = WiimoteConnector.conectWii(timeout); 
		wiimote.activateMotionSensing(); 
		wiimote.activateSmoothing(); 
		wiimote.setAlphaSmoothingValue(Float.parseFloat(PropMan.getInstance().getProperty(PropMan.WII_ALPHASMOOTHVALUE))); 
		wiimote.addWiiMoteEventListeners(new WiimoteMotionListener()); 
		
		GlobalObjects.getInstance().wiimote = wiimote; 
	}
}
