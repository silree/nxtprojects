package org.programus.nxj.rockboy.protocal;

import java.io.DataOutputStream;
import java.io.IOException;

public class Communicator implements ProtocalConst {
	public static void sendAngle(DataOutputStream dos, float angle) throws IOException {
		dos.writeByte(MT_ANGLE); 
		dos.writeFloat(angle); 
		dos.flush();
	}
	
	public static void sendCommand(DataOutputStream dos, int command) throws IOException {
		dos.writeByte(MT_COMMAND); 
		dos.writeInt(command); 
		dos.flush();
	}
	
	public static void sendNotify(DataOutputStream dos, byte notify) throws IOException {
		dos.writeByte(MT_NOTIFY); 
		dos.writeByte(notify); 
		dos.flush(); 
	}
}
