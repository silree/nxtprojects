package org.programus.nxj.rockboy.pc.nxt;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

public class NXTBTConnector {
	private DataOutputStream dos;
	private DataInputStream dis;
	private NXTConnector nxtConnector; 
	
	public synchronized boolean connectNXT(String nxtName, byte[] pin) throws NXTCommException {
		if (dos == null || dis == null) {
			NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH); 
			NXTInfo[] nxtInfos = nxtComm.search(nxtName, NXTCommFactory.BLUETOOTH); 
			if (nxtInfos.length > 0) {
				NXTInfo nxtInfo = nxtInfos[0]; 
				nxtConnector = new NXTConnector(); 
				boolean connected = nxtConnector.connectTo(nxtName, nxtInfo.deviceAddress, NXTCommFactory.BLUETOOTH); 
				if (connected) {
					dos = nxtConnector.getDataOut(); 
					dis = nxtConnector.getDataIn(); 
				}
			}
		}
		return this.isConnected(); 
	}
	
	/**
	 * @return the nxtConnector
	 */
	public NXTConnector getNxtConnector() {
		return nxtConnector;
	}

	public boolean isConnected() {
		return dos != null && dis != null; 
	}

	/**
	 * @return the dos
	 */
	public DataOutputStream getDos() {
		return dos;
	}

	/**
	 * @return the dis
	 */
	public DataInputStream getDis() {
		return dis;
	}
}
