package org.programus.nxj.rockboy.core.mc;

import java.awt.geom.Point2D;

/**
 * <p>Multi-Coordinate vector. </p>
 * This is a real vector, not a list-like class in java.util package. 
 * A vector has a absolute value and an angle (as McAngle object). 
 * One vector can be orthogonal decomposed into two components in LCD coordinate or natural coordinate. 
 * @author Programus
 * @see McAngle
 *
 */
public class McVector extends McObject {
	private double value; 
	private McAngle angle = new McAngle(); 
	private McUtil util = McUtil.getInstance(); 
	
	public McVector() {
	}
	
	public McVector(McVector vector) {
		this.copy(vector); 
	}
	
	public McVector(double value, McAngle angle) {
		this.setVector(value, angle); 
	}
	
	public void copy(McVector vector) {
		this.value = vector.value; 
		this.angle.copy(vector.angle); 
	}
	
	public void setVector(double value, McAngle angle) {
		this.value = value; 
		this.angle.setLcdAngle(angle.getLcdAngle()); 
	}
	
	public void setLcdVector(double value, double lcdAngle) {
		this.value = value; 
		this.angle.setLcdAngle(lcdAngle); 
	}
	
	public void setNaturalVector(double value, double naturalAngle) {
		this.value = value; 
		this.angle.setNaturalAngle(naturalAngle); 
	}
	
	public void setValue(double value) {
		this.value = value; 
	}
	
	public void setAngle(McAngle angle) {
		this.angle.setLcdAngle(angle.getLcdAngle()); 
	}
	
	/**
	 * Return the absolute value. 
	 * @return the absolute value. 
	 */
	public double getValue() {
		return this.value; 
	}
	
	/**
	 * Return the reference of the McAngle. 
	 * If you changed the value of the reference you get, it will affect the vector. 
	 * @return the reference of the McAngle. 
	 */
	public McAngle getMcAngleRef() {
		return this.angle; 
	}
	
	/**
	 * Return a clone of the McAngle. 
	 * Changes on the return value won't affect this vector. 
	 * @return a clone of the McAngle. 
	 */
	public McAngle getMcAngleClone() {
		return new McAngle(this.angle); 
	}
	
	public Point2D.Double getLcdOrthValues() {
		return util.polar2orth(this.value, this.angle.getLcdRadian()); 
	}
	
	public Point2D.Double getNaturalOrthValues() {
		return util.polar2orth(this.value, this.angle.getNaturalRadian()); 
	}
	
	public void setLcdOrthValues(double x, double y) {
		Point2D.Double p = util.orth2polar(x, y); 
		this.value = p.x; 
		this.angle.setLcdRadian(p.y); 
	}
	
	public void setLcdOrthValues(Point2D.Double point) {
		this.setLcdOrthValues(point.x, point.y); 
	}
	
	public void setNaturalOrthValues(double x, double y) {
		Point2D.Double p = util.orth2polar(x, y); 
		this.value = p.x; 
		this.angle.setNaturalRadian(p.y); 
	}
	
	public void setNaturalOrthValues(Point2D.Double point) {
		this.setNaturalOrthValues(point.x, point.y); 
	}
	
	/**
	 * @see McAngle#setStaticInLcd(boolean)
	 */
	public void setStaticInLcd(boolean s) {
		this.angle.setStaticInLcd(s); 
	}
	
	/**
	 * @see McAngle#isStaticInLcd()
	 */
	public boolean isStaticInLcd() {
		return this.angle.isStaticInLcd(); 
	}
	
	public McVector add(McVector vector) {
		if (this.value == 0) {
			this.value = vector.value; 
			this.angle.copy(vector.angle); 
		} else {
			double a = this.value; 
			double a2 = a * a; 
			double b = vector.value; 
			double b2 = b * b; 
			double angleC = this.angle.isStaticInLcd() == vector.angle.isStaticInLcd() ? 
					Math.PI + this.angle.getRawRadian() - vector.angle.getRawRadian() 
					: Math.PI + this.angle.getLcdRadian() - vector.angle.getLcdRadian(); 
			// Law of cosines
			double v2 = a2 + b2 - 2 * a * b * Math.cos(angleC); 
			double v = Math.sqrt(v2 > 0 ? v2 : 0); 
			// Law of sines
			double r = (v != 0) ? Math.asin(Math.sin(angleC) * b / v) : 0; 
			if (b2 > a2 + v2) {
				r = Math.PI - r; 
			}
			this.value = v; 
			if (this.angle.isStaticInLcd() == vector.angle.isStaticInLcd()) {
				this.angle.add(r); 
			} else {
				this.angle.turnCw(r); 
			}
		}
		return this; 
	}
	
	public McVector addNew(McVector vector) {
		return new McVector(this).add(vector); 
	}
	
	public McVector sub(McVector vector) {
		this.angle.add(Math.PI); 
		this.add(vector); 
		this.angle.sub(Math.PI); 
		return this; 
	}
	
	public McVector subNew(McVector vector) {
		return new McVector(this).sub(vector); 
	}
	
	@Override
	public String toString() {
		return new StringBuffer("McVector [")
			.append(this.value).append(',')
			.append(this.angle)
			.append(']')
			.toString(); 
	}

}
