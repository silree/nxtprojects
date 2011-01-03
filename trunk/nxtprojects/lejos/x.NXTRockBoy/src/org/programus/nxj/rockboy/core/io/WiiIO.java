package org.programus.nxj.rockboy.core.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.lcdui.Image;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.util.Delay;

import org.programus.nxj.rockboy.protocal.ProtocalConst;
import org.programus.nxj.util.DisplayUtil;
import org.programus.nxj.util.txtimg.TextImage;
import org.programus.nxj.util.txtimg.TextImage3x5;

public class WiiIO extends NXTIO implements ProtocalConst {
	
	private BTConnection btc; 
	private DataOutputStream dos;
	private DataInputStream dis;
	
	private int angle; 
	
	private boolean keepRequestAngle = true; 
	
	private class AngleRequestThread extends Thread {
		public AngleRequestThread() {
			this.setDaemon(true); 
		}
		public void run() {
			while(keepRequestAngle) {
				if (dos != null && dis != null) {
					synchronized(WiiIO.this) {
						try {
							dos.writeByte(NT_REQ_ANGLE); 
							dos.flush();
							double da = dis.readDouble(); 
							angle = da > .5 ? (int) da + 1 : (int) da;
						} catch (IOException e) {
							Sound.buzz(); 
						} 
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.programus.nxj.rockboy.core.io.NXTIO#getRotationAngle()
	 */
	@Override
	public int getRotationAngle() {
		return angle;
	}

	/* (non-Javadoc)
	 * @see org.programus.nxj.rockboy.core.io.NXTIO#resetPosition()
	 */
	@Override
	public void resetPosition() {
		TextImage ti = new TextImage3x5(); 
		Image waiting = ti.getImage("WAITING FOR BLUETOOTH..."); 
		Image connected = ti.getImage("BLUETOOTH CONNECTED!"); 
		LCD.clear();
		DisplayUtil.drawImageCenter(waiting, 0, LCD.ROP_COPY); 
		LCD.refresh(); 
		
		btc = Bluetooth.waitForConnection(); 
		dis = btc.openDataInputStream(); 
		dos = btc.openDataOutputStream(); 
		
		LCD.clear();
		DisplayUtil.drawImageCenter(connected, 0, LCD.ROP_COPY); 
		LCD.refresh(); 
		new AngleRequestThread().start(); 
		Sound.beepSequenceUp(); 
	}

	/* (non-Javadoc)
	 * @see org.programus.nxj.rockboy.core.io.IOModule#stop()
	 */
	@Override
	public void stop() {
		keepRequestAngle = false;
		Delay.msDelay(100); 
		synchronized(this) {
			try {
				dos.writeByte(NT_CLOSE_CONNECTION); 
				dos.flush();
				byte data = dis.readByte(); 
				if (data == MT_NULL) {
					Sound.beep();
				}
			} catch (IOException e) {
				Sound.buzz(); 
			} 
		}
	}
}
