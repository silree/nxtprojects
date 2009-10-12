package org.programus.nxj.rockboy.core.mc;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.programus.nxj.rockboy.core.input.InputModule;

public class McUtil {
	private static McUtil util = new McUtil(); 
	private McUtil() {
	}
	public static McUtil getInstance() {
		return util; 
	}
	
	private int angle; 
	private InputModule input = InputModule.getInputModule(); 
	
	public static double SCREEN_BOTTOM_OFFSET = 336. / 11; 
	
	public void updateAngle() {
		this.angle = this.input.getRotationAngle(); 
	}
	
	public int getAngle() {
		return this.angle; 
	}
	
	public double getRadian() {
		return this.angle2radian(this.angle); 
	}
	
	public double angle2radian(double angle) {
		return angle * Math.PI / 180; 
	}
	
	public double radian2angle(double radian) {
		return 180 * radian / Math.PI; 
	}
	
	public Point2D.Double polar2orth(double l, double r) {
		double x = l * Math.cos(r); 
		double y = l * Math.sin(r); 
		return new Point2D.Double(x, y); 
	}
	
	public Point2D.Double orth2polar(double x, double y) {
		double l = Math.sqrt(x * x + y * y); 
		double r = Math.atan(y / x); 
		if (x < 0) {
			r += Math.PI; 
		}
		return new Point2D.Double(l, r); 
	}
	
	public Point getLcdCooridnateOffset() {
		return this.input.getScreenBound(); 
	}
}
