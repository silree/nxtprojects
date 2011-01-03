package org.programus.nxj.rockboy.pc.comm;

import lejos.pc.comm.NXTCommException;

import org.programus.nxj.rockboy.pc.PropMan;
import org.programus.nxj.rockboy.pc.nxt.NXTBTConnector;

public class NXTPrepareAction implements Runnable {

	@Override
	public void run() {
		// Prepare necessary elements.
		NXTBTConnector connector = new NXTBTConnector(); 
		PropMan p = PropMan.getInstance(); 
		String nxtName = p.getProperty(PropMan.NXT_NAME); 
		byte[] pin = this.parsePin(p.getProperty(PropMan.NXT_PIN)); 
		long timeout = Long.parseLong(p.getProperty(PropMan.NXT_CONN_TIMEOUT)); 
		long startTime = System.currentTimeMillis(); 
		boolean connected = false; 
		
		// Trying...
		System.out.println(); 
		System.out.print("Trying to connect to NXT ++"); 
		while (!connected && (timeout <= 0 || System.currentTimeMillis() < startTime + timeout)) {
			System.out.print('+'); 
			try {
				connected = connector.connectNXT(nxtName, pin);
			} catch (NXTCommException e) {
				System.err.println("Something is wrong when connect to NXT");
				e.printStackTrace(); 
				break; 
			} 
		}
		
		if (connected) {
			GlobalObjects go = GlobalObjects.getInstance(); 
			go.nxtConnector = connector.getNxtConnector(); 
			go.dis = connector.getDis(); 
			go.dos = connector.getDos(); 
			System.out.println("NXT Connected!"); 
		} else {
			System.err.println("NXT Connect interrupted!"); 
		}
	}
	
	private byte[] parsePin(String strPin) {
		byte[] bytes = strPin.getBytes(); 
		byte[] ret = new byte[bytes.length]; 
		for (int i = 0; i < bytes.length; i++) {
			ret[i] = (byte)(bytes[i] - '0'); 
		}
		return ret; 
	}
}
