package org.programus.nxj.rockboy.core.mc;

/**
 * Multi-Coordinate angle. 
 * Default value is LCD coordinate radian value. 
 * @author Programus
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
	
	public McAngle(McAngle angle) {
		this.copy(angle); 
	}
	
	public void copy(McAngle angle) {
		this.radian = angle.radian; 
		this.staticInLcd = angle.staticInLcd; 
	}
	
	public double getRawRadian() {
		return this.radian;
	}
	
	public double getRawAngle() {
		return util.radian2angle(this.radian); 
	}
	
	public boolean isStaticInLcd() {
		return this.staticInLcd; 
	}
	
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
	
	public McAngle add(McAngle angle) {
		this.radian += angle.radian; 
		return this; 
	}
	
	public McAngle addNew(McAngle angle) {
		return new McAngle(this).add(angle); 
	}
	
	public McAngle sub(McAngle angle) {
		this.radian -= angle.radian; 
		return this; 
	}
	
	public McAngle subNew(McAngle angle) {
		return new McAngle(this).sub(angle); 
	}
	
	public McAngle add(double radian) {
		this.radian += radian; 
		return this; 
	}
	
	public McAngle addNew(double radian) {
		return new McAngle(this).add(radian); 
	}
	
	public McAngle sub(double radian) {
		return this.add(-radian); 
	}
	
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
	
	public McAngle turnCcNew(double radian) {
		return new McAngle(this).turnCc(radian); 
	}
	
	public McAngle reverse() {
		this.radian = - this.radian; 
		return this; 
	}
	
	public McAngle reverseNew() {
		return new McAngle(this).reverse(); 
	}
	
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
