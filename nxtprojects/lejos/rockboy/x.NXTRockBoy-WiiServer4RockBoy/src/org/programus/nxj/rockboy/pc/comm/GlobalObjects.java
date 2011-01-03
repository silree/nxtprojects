package org.programus.nxj.rockboy.pc.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.pc.comm.NXTConnector;
import wiiusej.Wiimote;

public class GlobalObjects {
	Wiimote wiimote; 
	NXTConnector nxtConnector;
	DataOutputStream dos;
	DataInputStream dis;
	BTServer btSvr;
	
	private static GlobalObjects go = new GlobalObjects(); 
	
	private GlobalObjects(){
		Thread wiiConnThread = new Thread(new WiimotePrepareAction(), "Wii Connecting Thread"); 
		wiiConnThread.start(); 
		btSvr = new BTServer(); 
		btSvr.start(); 
	}
	
	public static GlobalObjects getInstance() {
		return go;
	}
	
	/**
	 * @return the DataOutpuStream for output data to NXT.
	 */
	public synchronized DataOutputStream getDos() {
		return dos;
	}
	
	/**
	 * @return the DataInpuStream to get data from NXT.
	 */
	public synchronized DataInputStream getDis() {
		return dis;
	}
}
