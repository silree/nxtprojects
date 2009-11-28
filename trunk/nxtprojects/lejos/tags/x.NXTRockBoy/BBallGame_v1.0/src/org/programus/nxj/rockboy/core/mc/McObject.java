package org.programus.nxj.rockboy.core.mc;

/**
 * Super class of all multi-coordinate classes. 
 * @author Programus
 * @see org.programus.nxj.rockboy.core.mc
 *
 */
public abstract class McObject {
	/**
	 * @see McAngle#isStaticInLcd()
	 */
	abstract public boolean isStaticInLcd(); 
	/**
	 * @see McAngle#setStaticInLcd(boolean)
	 */
	abstract public void setStaticInLcd(boolean s); 
}
