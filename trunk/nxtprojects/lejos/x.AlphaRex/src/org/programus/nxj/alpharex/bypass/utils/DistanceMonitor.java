package org.programus.nxj.alpharex.bypass.utils;

import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.comm.RConsole;

public class DistanceMonitor implements Runnable, SensorPortListener {

	private final static int CAPACITY = 10; 
	public final static int MAX = 0xff; 
	private int[] pool = new int[CAPACITY]; 
	private int cursor = 0; 
	private Thread monitorThread; 
	private boolean pause = false; 
	
	private static DistanceMonitor monitor = new DistanceMonitor(); 
	private DistanceMonitor() {
		RConsole.println("DistanceMonitor constructor"); 
		for (int i = 0; i < this.pool.length; i++) {
			this.pool[i] = MAX; 
		}
	}
	
	public static DistanceMonitor getInstance() {
		return monitor; 
	}
	
	public void startMonitoring() {
		if (this.monitorThread == null) {
			this.monitorThread = new Thread("MonitorDistance", monitor); 
			this.monitorThread.setDaemon(true); 
			this.monitorThread.setPriority(Thread.NORM_PRIORITY + 1); 
		}
		RConsole.println("Start monitor:" + this.monitorThread); 
		this.monitorThread.start(); 
		RConsole.println("Monitor started"); 
	}
	
	public int getDistance() {
		int min = MAX; 
		synchronized(this.pool) {
			for (int d : this.pool) {
				if (d < min) {
					min = d; 
				}
			}
		}
		return min; 
	}
	
	public int getLatestDistance() {
		return this.pool[this.cursor - 1]; 
	}
	
	public synchronized void setPause(boolean pause) {
		this.pause = pause; 
	}
	
	public synchronized boolean isPause() {
		return this.pause; 
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer(); 
		synchronized(this.pool) {
			for (int d : this.pool) {
				sb.append(d).append(','); 
			}
		}		
		return sb.toString(); 
	}
	
	@Override
	public void run() {
		RConsole.println("Runing distance monitor"); 
		while (true) {
//			RConsole.println("geting distance..." + !this.pause + ", " + cursor); 
			if (!pause) {
				int distance = Robot.SONAR_S.getDistance(); 
//				RConsole.println("distance: " + distance); 
				synchronized(this.pool) {
					this.pool[cursor++] = distance > 0 ? distance : MAX; 
				}
				cursor %= this.pool.length; 
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {} 
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {} 
			}
		}
	}

	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		RConsole.println("US data changed: " + aOldValue + ", " + aNewValue); 
	}

}
