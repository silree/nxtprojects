package org.programus.nxj.rockboy.core.mc;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * <p>Multi-Coordinate point. </p>
 * The staticInLcd property will decide whether this point is static in LCD coordinate. 
 * <p>
 * The internal storage of a McPoint is not x, y values in any coordinate, but a vector. 
 * The origin of the vector is nature coordinate origin. 
 * </p>
 * @author Programus
 *
 */
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
	
	/**
	 * Return the vector start from this point to the specified point. 
	 * @param p
	 * @return the vector start from this point to the specified point. 
	 */
	public McVector getVectorTo(McPoint p) {
		return p.vectorValue.subNew(this.vectorValue); 
	}
	
	/**
	 * Return the distance to the specified point. 
	 * @param p
	 * @return the distance to the specified point. 
	 */
	public double getDistanceTo(McPoint p) {
		return this.getVectorTo(p).getValue(); 
	}
	
	@Deprecated
	public McAngle getAngleTo(McPoint p) {
		return this.getVectorTo(p).getMcAngleClone(); 
	}
	
	/**
	 * Return the distance from this point to the line which passes through the specified two points. 
	 * @param p1 one point in the line
	 * @param p2 another point in the line
	 * @return the distance from this point to the line which passes through the specified two points. 
	 */
	public double getDistanceToLine(McPoint p1, McPoint p2) {
		McVector v1 = p2.getVectorTo(p1); 
		McVector v = p2.getVectorTo(this); 
		v.setStaticInLcd(v1.isStaticInLcd()); 
		v.getMcAngleRef().sub(v1.getMcAngleRef().getRawRadian()); 
		Point2D.Double p = v.isStaticInLcd() ? v.getLcdOrthValues() : v.getNaturalOrthValues(); 
		double d = Math.abs(p.y); 
		return p.x > 0 && p.x <= v1.getValue() ? d : -d; 
	}
	
	/**
	 * Move this point along the specified vector. 
	 * @param vector
	 * @return itself
	 */
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

}
