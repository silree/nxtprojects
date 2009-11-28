package org.programus.nxj.rockboy.core.thing;

import java.awt.Point;
import java.awt.geom.Point2D;

import lejos.nxt.comm.RConsole;

import org.programus.nxj.rockboy.core.mc.McUtil;

public class BoundaryLoopCalculator {
	public final static int X_LOOP_MARK = 1; 
	public final static int Y_LOOP_MARK = 1 << 1; 
	public final static int BOTH_LOOP_MARK = X_LOOP_MARK | Y_LOOP_MARK; 
	
	private static Point boundary = McUtil.getInstance().getLcdCooridnateOffset(); 
	
	private Thing master; 
	private int loopMark; 
	
	public BoundaryLoopCalculator(Thing master, int loopMark) {
		this.master = master; 
		this.loopMark = loopMark; 
	}
	
	public boolean loop() {
		Point2D.Double p = this.master.centerPoint.getLcdPoint(); 
		boolean looped = false; 
		if ((this.loopMark & X_LOOP_MARK) > 0) {
			double t = p.x; 
			if (p.x >= boundary.x) {
				p.x %= boundary.x; 
			}
			if (p.x < 0) {
				p.x += boundary.x; 
			}
			
			looped = (t != p.x); 
		}
		
		if ((this.loopMark & Y_LOOP_MARK) > 0) {
			double t = p.y; 
			if (p.y >= boundary.y) {
				p.y %= boundary.y; 
			}
			if (p.y < 0) {
				p.y += boundary.y; 
			}
			
			looped = (t != p.y); 
		}
		
		if (looped) {
			this.master.centerPoint.setLcdPoint(p); 
		}
		
		return looped; 
	}
}
