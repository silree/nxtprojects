package org.programus.nxj.rockboy.pc.comm;

import java.io.IOException;

import wiiusej.WiiUseApiManager;

public class ShutdownHook implements Runnable {

	@Override
	public void run() {
		GlobalObjects go = GlobalObjects.getInstance(); 
		if (go.btSvr != null && go.btSvr.isAlive()) {
			go.btSvr.shutdown(); 
		}
		if (go.wiimote != null) {
			go.wiimote.disconnect(); 
		}
		WiiUseApiManager.shutdown(); 
	}

}
