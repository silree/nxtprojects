package org.programus.nxj.rockboy.pc.comm;

import java.io.IOException;

import org.programus.nxj.rockboy.pc.wii.WiimoteData;
import org.programus.nxj.rockboy.protocal.ProtocalConst;

public class BTServer extends Thread implements ProtocalConst {
	private boolean ongoing = true; 
	public void run() {
		while (true) {
			GlobalObjects go = GlobalObjects.getInstance(); 
			Thread nxtConnThread = new Thread(new NXTPrepareAction(), "NXT Connecting Thread"); 
			nxtConnThread.start(); 
			while (ongoing && (go.dis == null || go.dos == null)) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					return; 
				} 
			}
			WiimoteData wd = WiimoteData.getInstance(); 
			while (ongoing && (go.dis != null && go.dos != null)) {
				try {
					byte notify = go.dis.readByte();
					switch (notify) {
					case NT_REQ_ANGLE:
						go.dos.writeDouble(wd.getAngle4NXT()); 
						go.dos.flush(); 
						break;
					case NT_CLOSE_CONNECTION:
						go.dos.writeByte(MT_NULL); 
						go.dos.flush(); 
						this.restart(); 
						break;
					}
				} catch (IOException e) {
					e.printStackTrace(); 
					return; 
				} 
			}
		}
	}
	
	public synchronized void shutdown() {
		System.out.println("Shuting down..."); 
		GlobalObjects go = GlobalObjects.getInstance(); 
		try {
			go.dos.close();
			go.dis.close();
			go.nxtConnector.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		go.dos = null;
		go.dis = null;
		go.nxtConnector = null;
		ongoing = false;
	}
	
	public synchronized void restart() {
		this.shutdown();
		System.out.println("Restarting..."); 
		ongoing = true; 
		new Thread(new Runnable(){
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					return;
				} 
				BTServer.this.start(); 
			}
		}); 
	}
}
