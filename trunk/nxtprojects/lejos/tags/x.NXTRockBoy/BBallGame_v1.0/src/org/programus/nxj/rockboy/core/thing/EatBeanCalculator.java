package org.programus.nxj.rockboy.core.thing;

import java.awt.geom.Point2D;
import java.util.List;

import org.programus.nxj.rockboy.core.mc.McPoint;

/**
 * This class is used to process the eat bean action for its master. 
 * @author Programus
 *
 */
public class EatBeanCalculator {
	private Thing master; 
	private List<Point2D.Double> beanList; 
	private int distanceLimit; 
	
	/**
	 * <p>Constructor. </p>
	 * You must specify its master, normal is the ball, and the bean list. 
	 * When a bean is eaten, the bean will be removed from the list. 
	 * @param t
	 * @param beanList
	 */
	public EatBeanCalculator(Thing t, List<Point2D.Double> beanList) {
		this.master = t; 
		this.beanList = beanList; 
		this.distanceLimit = ((this.master.getWidth() + this.master.getHeight()) >> 2) + 1; 
	}
	
	/**
	 * To know whether a bean is eaten. Once a bean is eaten, it will also be removed from the list.
	 * @param oldPoint the previous point of the master thing
	 * @return true if eaten. 
	 */
	public boolean eat(McPoint oldPoint) {
		McPoint beanPoint = new McPoint(); 
		beanPoint.setStaticInLcd(true); 
		synchronized (this.beanList) {
			int index = this.beanList.size() - 1; 
			beanPoint.setLcdPoint(this.beanList.get(index)); 
			
			double distance = beanPoint.getDistanceToLine(oldPoint, this.master.centerPoint); 
			boolean ret = (distance >= 0.00001) && (distance <= this.distanceLimit); 
			if (ret) {
				this.beanList.remove(index); 
			}
			return ret; 
		}
	}
}
