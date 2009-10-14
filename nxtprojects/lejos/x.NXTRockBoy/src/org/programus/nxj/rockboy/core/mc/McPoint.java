package org.programus.nxj.rockboy.core.mc;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lejos.nxt.comm.RConsole;


public class McPoint extends McObject {
	public final static double X_OFFSET; 
	public final static double Y_OFFSET; 
	private static McUtil util; 
	
	static {
		util = McUtil.getInstance(); 
		Point p = util.getLcdCooridnateOffset(); 
		X_OFFSET = (p.x >> 1); 
		Y_OFFSET = p.y + McUtil.SCREEN_BOTTOM_OFFSET; 
	}
	
	private McVector vectorValue = new McVector(); 
	public McPoint() {}
	
	public McPoint(McPoint point) {
		this.copy(point); 
	}
	
	
	public void copy(McPoint point) {
		this.vectorValue = new McVector(point.vectorValue);  
	}
	
	public McVector getVectorRef() {
		return this.vectorValue; 
	}
	
	public McVector getVectorClone() {
		return new McVector(this.vectorValue); 
	}
	
	public void setStaticInLcd(boolean s) {
		this.vectorValue.setStaticInLcd(s); 
	}
	
	public boolean isStaticInLcd() {
		return this.vectorValue.isStaticInLcd(); 
	}
	
	public void setNaturalPoint(double x, double y) {
		this.vectorValue.setNaturalOrthValues(x, y); 
	}
	
	public void setNatualPoint(Point2D.Double p) {
		this.vectorValue.setNaturalOrthValues(p); 
	}
	
	public Point2D.Double getNaturalPoint() {
		return this.vectorValue.getNaturalOrthValues(); 
	}
	
	public void setLcdPoint(double x, double y) {
		x -= X_OFFSET; 
		y -= Y_OFFSET; 
		this.vectorValue.setLcdOrthValues(x, y); 
	}
	
	public void setLcdPoint(Point2D.Double p) {
		this.setLcdPoint(p.x, p.y); 
	}
	
	public Point2D.Double getLcdPoint() {
		Point2D.Double p = this.vectorValue.getLcdOrthValues(); 
		p.x += X_OFFSET; 
		p.y += Y_OFFSET; 
		return p; 
	}
	
	public McVector getVectorTo(McPoint p) {
		return p.vectorValue.subNew(this.vectorValue); 
	}
	
	public double getDistanceTo(McPoint p) {
		return this.getVectorTo(p).getValue(); 
	}
	
	public McAngle getAngleTo(McPoint p) {
		return this.getVectorTo(p).getMcAngleClone(); 
	}
	
	public double getDistanceToLine(McPoint p1, McPoint p2) {
		McVector v1 = p2.getVectorTo(p1); 
		McVector v = p2.getVectorTo(this); 
		v.setStaticInLcd(v1.isStaticInLcd()); 
		v.getMcAngleRef().sub(v1.getMcAngleRef().getRawRadian()); 
		Point2D.Double p = v.isStaticInLcd() ? v.getLcdOrthValues() : v.getNaturalOrthValues(); 
		double d = Math.abs(p.y); 
		return p.x > 0 && p.x <= v1.getValue() ? d : -d; 
	}
	
	public McPoint move(McVector vector) {
		this.vectorValue.add(vector); 
		return this; 
	}
	
	public McPoint moveNew(McVector vector) {
		return new McPoint(this).move(vector); 
	}
	
	public McPoint back(McVector vector) {
		this.vectorValue.sub(vector); 
		return this; 
	}
	
	public McPoint backNew(McVector vector) {
		return new McPoint(this).back(vector); 
	}
	
	public McPoint turnCw(double radian) {
		this.vectorValue.getMcAngleRef().turnCw(radian); 
		return this; 
	}
	
	public McPoint turnCwNew(double radian) {
		return new McPoint(this).turnCw(radian); 
	}
	
	public McPoint turnCc(double radian) {
		this.vectorValue.getMcAngleRef().turnCc(radian); 
		return this; 
	}
	
	public McPoint turnCcNew(double radian) {
		return new McPoint(this).turnCc(radian); 
	}
	
	@Override
	public String toString() {
		return new StringBuffer("McPoint{")
			.append("L: ")
			.append(this.getLcdPoint())
			.append("/")
			.append("N: ")
			.append(this.getNaturalPoint())
			.append("|")
			.append(this.vectorValue)
			.append("}").toString(); 
	}
	
	public static void main(String[] args) {
//		McUtil.getInstance().updateAngle(); 
//		McPoint p1 = new McPoint(); 
//		McPoint p2 = new McPoint(); 
//		McPoint p = new McPoint(); 
//		p1.setStaticInLcd(true); 
//		p2.setStaticInLcd(true); 
//		p.setStaticInLcd(true); 
//		p1.setLcdPoint(92.1955122729339, 7.18847524415601); 
//		p2.setLcdPoint(93.4253407287673, 6.93505465286158); 
//		p.setLcdPoint(36, 19); 
//		System.out.println(p.getDistanceToLine(p1, p2)); 

		List<Rectangle> obstacleList = new ArrayList<Rectangle>(); 
		obstacleList.add(new Rectangle(20, 20, 50, 10)); 
		obstacleList.add(new Rectangle(70, 20, 10, 44)); 
		
		Random rand = new Random(); 
		List<Point2D.Double> beans = new ArrayList<Point2D.Double>(); 
		for (int i = 0; i < 5; i++) {
			Point2D.Double p = new Point2D.Double(); 
			boolean availablePoint = false; 
			while (!availablePoint) {
				p.setLocation(rand.nextInt(100), rand.nextInt(64)); 
				availablePoint = true; 
				for (Rectangle rect : obstacleList) {
					if (rect.contains(p)) {
						availablePoint = false; 
						break; 
					}
				}
			}
			
			beans.add(p); 
			System.out.println(p.toString()); 
		}
}
}
