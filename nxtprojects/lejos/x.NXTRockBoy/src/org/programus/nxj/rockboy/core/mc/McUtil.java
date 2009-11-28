package org.programus.nxj.rockboy.core.mc;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.programus.nxj.rockboy.core.io.IOModule;

/**
 * A utility class for all multi-coordinate processes. 
 * @author Programus
 *
 */
public class McUtil {
	private static McUtil util = new McUtil(); 
	private McUtil() {
	}
	public static McUtil getInstance() {
		return util; 
	}
	
	private int angle; 
	private IOModule input = IOModule.getIOModule(); 
	
	public static double SCREEN_BOTTOM_OFFSET = 336. / 11; 
	
	/**
	 * Update the angle to the current NXT brick rotation angle. 
	 */
	public void updateAngle() {
		this.angle = this.input.getRotationAngle(); 
	}
	
	/**
	 * Return the rotation angle of the NXT brick. 
	 * @return
	 */
	public int getAngle() {
		return this.angle; 
	}
	
	/**
	 * Return the rotation radian of the NXT brick. 
	 * @return
	 */
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
		double r = x == 0 ? 0 : Math.atan(y / x); 
		if (x < 0) {
			r += Math.PI; 
		}
		return new Point2D.Double(l, r); 
	}
	
	public Point getLcdCooridnateOffset() {
		return this.input.getScreenBoundary(); 
	}
}
