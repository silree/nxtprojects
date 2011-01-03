package org.programus.nxj.rockboy.pc.wii;

import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;

public class WiimoteConnector {
	public static Wiimote conectWii(long timeout) {
		long startTime = System.currentTimeMillis(); 
		Wiimote[] wiimotes = null; 
		System.out.println(); 
		System.out.print("Trying to connect to wii."); 
		while ((wiimotes == null || wiimotes.length <= 0) && (timeout <= 0 || System.currentTimeMillis() < startTime + timeout)) {
			System.out.print('.'); 
			wiimotes = WiiUseApiManager.getWiimotes(1, true); 
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break; 
			}
		}
		System.out.println(); 
		
		Wiimote wm = null; 
		if (wiimotes.length > 0) {
			wm = wiimotes[0]; 
			System.out.println("Wiimote Connected!"); 
			System.out.println(wm); 
		} else {
			System.err.println("Wiimote Connect interrupted!"); 
		}
		
		return wm; 
	}
	
	public static void main(String[] args) {
		Wiimote wm = conectWii(0); 
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		} 
		System.out.println("disconnect"); 
		wm.disconnect(); 
		WiiUseApiManager.shutdown(); 
	}
}
