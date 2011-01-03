package org.programus.nxj.rockboy.protocal;

public interface ProtocalConst {
	byte MT_NULL = 0;
	byte MT_ANGLE = 1;
	byte MT_COMMAND = 2;
	byte MT_NOTIFY = (byte) 0xf0;
	
	byte NT_CLOSE_CONNECTION = 9; 
	byte NT_REQ_ANGLE = 1; 
	byte NT_REQ_COMMAND = 2;
	byte NT_REQ_PARAM = 3;
}
