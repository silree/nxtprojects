package org.programus.nxj.rockboy.core.mc;

/**
 * <p>Multi-Coordinate angle. </p>
 * <p>The angle values of two coordinates are opposite to each other, 
 * since the positive direction of y-axis are different. </p>
 * <p>You can retrieve a value in angle or radian. The data stored in class is in radian. </p>
 * 
 * @author Programus
 * @see org.programus.nxj.rockboy.core.mc
 *
 */
public class McAngle extends McObject {
	private final static double R = 2 * Math.PI; 
	private double radian; 
	private boolean staticInLcd = true; 
	
	private McUtil util = McUtil.getInstance(); 
	
	public McAngle() {
	}
	
	public McAngle(double lcdAngle) {
		this.setLcdAngle(lcdAngle); 
	}
	
	/**
	 * Copy constructor. 
	 * @param angle copy source
	 */
	public McAngle(McAngle angle) {
		this.copy(angle); 
	}
	
	/**
	 * Make this object a copy of specified McAngle object. 
	 * A copy means they have the same value but not the same reference. 
	 * @param angle
	 */
	public void copy(McAngle angle) {
		this.radian = angle.radian; 
		this.staticInLcd = angle.staticInLcd; 
	}
	
	/**
	 * Return the internal stored radian value. 
	 * It is the value in LCD coordinate or nature coordinate according to the staticInLcd property. 
	 * @return
	 */
	public double getRawRadian() {
		return this.radian;
	}
	
	/**
	 * Return the angle value of the internal stored radian. 
	 * @return
	 */
	public double getRawAngle() {
		return util.radian2angle(this.radian); 
	}
	
	/**
	 * Tell you whether this angle is static in LCD coordinate. 
	 * Being static in LCD coordinate means the internal stored radian is the radian in LCD coordinate. 
	 * @return true if the internal stored radian is LCD coordinate radian. 
	 */
	public boolean isStaticInLcd() {
		return this.staticInLcd; 
	}
	
	/**
	 * Set the "static in LCD" property. 
	 * "static in LCD" means the internal stored radian is the radian in LCD coordinate. 
	 * It is recommend that set this value true if you want the angle rotate with NXT brick 
	 * and set this value false if you want the angle looks like just stay there no matter 
	 * whether the NXT brick is rotated or not. Do so can reduce the burden of the CPU. 
	 * @param s "static in LCD" property. 
	 */
	public void setStaticInLcd(boolean s) {
		if (this.staticInLcd != s) {
			this.radian = 
				this.staticInLcd 
					? this.lcd2naturalRadian(this.radian) 
					: this.natural2lcdRadian(this.radian); 
			this.staticInLcd = s; 
		}
	}
	
	private double lcd2naturalRadian(double radian) {
		return -radian - util.getRadian(); 
	}
	
	private double lcd2naturalAngle(double angle) {
		return -angle - util.getAngle(); 
	}
	
	private double natural2lcdRadian(double radian) {
		return -radian - util.getRadian(); 
	}
	
	private double natural2lcdAngle(double angle) {
		return -angle - util.getAngle(); 
	}
	
	public void setLcdAngle(double angle) {
		this.radian = util.angle2radian(this.isStaticInLcd() ? angle : this.lcd2naturalAngle(angle));  
	}
	
	public double getLcdAngle() {
		return util.radian2angle(this.isStaticInLcd() ? this.radian : this.natural2lcdRadian(this.radian)); 
	}
	
	public void setLcdRadian(double radian) {
		this.radian = this.isStaticInLcd() ? radian : this.lcd2naturalRadian(radian); 
	}
	
	public double getLcdRadian() {
		return this.isStaticInLcd() ? this.radian : this.natural2lcdRadian(this.radian); 
	}
	
	public void setNaturalAngle(double angle) {
		this.radian = util.angle2radian(this.isStaticInLcd() ? this.natural2lcdAngle(angle) : angle); 
	}
	
	public double getNaturalAngle() {
		return this.isStaticInLcd() ? this.lcd2naturalAngle(util.radian2angle(this.radian)) : util.radian2angle(this.radian); 
	}
	
	public void setNaturalRadian(double radian) {
		this.radian = this.isStaticInLcd() ? this.natural2lcdRadian(radian) : radian; 
	}
	
	public double getNaturalRadian() {
		return this.isStaticInLcd() ? this.lcd2naturalRadian(this.radian) : this.radian; 
	}
	
	/**
	 * Just add an angle into this angle. This angle will be changed. 
	 * The effect of this method invocation would be turn clockwise if staticInLcd is true. 
	 * @param angle
	 * @return this object itself
	 */
	public McAngle add(McAngle angle) {
		this.radian += angle.radian; 
		return this; 
	}
	
	/**
	 * The angle of this angle add another angle and return the result as a new McAngle object. 
	 * This angle won't be changed. 
	 * @param angle
	 * @return
	 * @see #add(McAngle)
	 */
	public McAngle addNew(McAngle angle) {
		return new McAngle(this).add(angle); 
	}
	
	/**
	 * subtraction an angle. 
	 * @param angle
	 * @return
	 * @see #add(McAngle)
	 */
	public McAngle sub(McAngle angle) {
		this.radian -= angle.radian; 
		return this; 
	}
	
	/**
	 * subtranction an angle and return a new object
	 * @param angle
	 * @return
	 * @see #addNew(McAngle)
	 */
	public McAngle subNew(McAngle angle) {
		return new McAngle(this).sub(angle); 
	}
	
	/**
	 * @see #add(McAngle)
	 */
	public McAngle add(double radian) {
		this.radian += radian; 
		return this; 
	}
	
	/**
	 * @see #addNew(McAngle)
	 */
	public McAngle addNew(double radian) {
		return new McAngle(this).add(radian); 
	}
	
	/**
	 * @see #sub(McAngle)
	 */
	public McAngle sub(double radian) {
		return this.add(-radian); 
	}
	
	/**
	 * @see #subNew(McAngle)
	 */
	public McAngle subNew(double radian) {
		return new McAngle(this).sub(radian); 
	}
	
	/**
	 * Turn clockwise
	 * @param radian
	 * @return
	 */
	public McAngle turnCw(double radian) {
		if (this.isStaticInLcd()) {
			this.radian += radian; 
		} else {
			this.radian -= radian; 
		}
		return this; 
	}
	
	/**
	 * @see #turnCw(double)
	 * @see #addNew(McAngle)
	 */
	public McAngle turnCwNew(double radian) {
		return new McAngle(this).turnCw(radian); 
	}

	/**
	 * Turn counter-clockwise
	 * @param radian
	 * @return
	 */
	public McAngle turnCc(double radian) {
		return this.turnCw(-radian); 
	}
	
	/**
	 * @see #turnCwNew(double)
	 */
	public McAngle turnCcNew(double radian) {
		return new McAngle(this).turnCc(radian); 
	}
	
	/**
	 * (current value) -> -(current value)
	 * @return -current value
	 */
	public McAngle reverse() {
		this.radian = - this.radian; 
		return this; 
	}
	
	public McAngle reverseNew() {
		return new McAngle(this).reverse(); 
	}
	
	/**
	 * Ex. 
	 * <ul>
	 * <li>720 degree -> 0 degree</li>
	 * <li>368 degree -> 8 degree</li>
	 * <li>-735 degree -> -15 degree</li>
	 * </ul>
	 * @return
	 */
	public McAngle ltRound() {
		this.radian %= R; 
//		double r = this.radian > 0 ? -R : R; 
//		while (!(this.radian >= 0 && this.radian < R)) {
//			this.radian += r; 
//		}
		return this; 
	}
	
	public McAngle ltRoundNew() {
		return new McAngle(this).ltRound(); 
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (this.isStaticInLcd()) {
			return new StringBuffer("L:")
				.append(this.getRawAngle())
				.append("/N:")
				.append(this.getNaturalAngle()).toString(); 
		} else {
			return new StringBuffer("N:")
			.append(this.getRawAngle())
			.append("/L:")
			.append(this.getLcdAngle()).toString(); 
		}
	}

}
