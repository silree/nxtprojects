package org.programus.nxj.rockboy.core.thing;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.microedition.lcdui.Image;

import org.programus.nxj.rockboy.core.mc.McPoint;
import org.programus.nxj.rockboy.core.mc.McVector;

public abstract class Thing {
	protected Rectangle2D.Double rect = new Rectangle2D.Double(); 
	protected McPoint centerPoint = new McPoint(); 
	protected McVector speed = new McVector(); 
	
	private void resetRect() {
		Point2D.Double p = this.centerPoint.getLcdPoint(); 
		rect.x = p.x - (double)this.getWidth() / 2; 
		rect.y = p.y - (double)this.getHeight() / 2; 
		this.rect.width = this.getImage().getWidth(); 
		this.rect.height = this.getImage().getHeight(); 
	}
	
	public McPoint getCenterPointRef() {
		return this.centerPoint; 
	}
	
	public McPoint getCenterPointClone() {
		return new McPoint(this.centerPoint); 
	}
	
	public void setCenterPoint(McPoint point) {
		this.centerPoint.copy(point); 
	}
	
	public void setLcdCenterPoint(double x, double y) {
		this.centerPoint.setLcdPoint(x, y); 
	}
	
	public void setLcdCenterPoint(Point2D.Double point) {
		this.centerPoint.setLcdPoint(point); 
	}
	
	public void setNaturalCenterPoint(double x, double y) {
		this.centerPoint.setNaturalPoint(x, y); 
	}
	
	public void setNaturalCenterPoint(Point2D.Double point) {
		this.centerPoint.setNatualPoint(point); 
	}
	
	public void setSpeed(McVector speed) {
		this.speed.copy(speed); 
	}
	
	public McVector getSpeedRef() {
		return this.speed; 
	}
	
	public McVector getSpeedClone() {
		return new McVector(this.speed); 
	}
	
	public void moveOneStep() {
		this.centerPoint.move(this.speed); 
	}
	
	public Point getTopLeft() {
		Point2D.Double p = this.centerPoint.getLcdPoint(); 
		Point topLeft = new Point((int)Math.round(p.x), (int)Math.round(p.y)); 
		topLeft.x -= (this.getWidth() >> 1); 
		topLeft.y -= (this.getHeight() >> 1); 
		
		return topLeft; 
	}
	
	public int getWidth() {
		return this.getImage().getWidth(); 
	}
	
	public int getHeight() {
		return this.getImage().getHeight(); 
	}
	
	protected Rectangle2D.Double getRect() {
		this.resetRect(); 
		return this.rect; 
	}
	
	public boolean contains(Point2D p) {
		this.resetRect(); 
		return this.rect.contains(p); 
	}
	
	public boolean contains(Thing t) {
		this.resetRect(); 
		return this.rect.contains(t.rect); 
	}
	
	public boolean intersects(Thing t) {
		this.resetRect(); 
		return this.rect.intersects(t.rect); 
	}
	
	public abstract void run();	
	public abstract Image getImage(); 
}
