package org.programus.nxj.rockboy.games.bball.objects;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.List;

import javax.microedition.lcdui.Image;

import org.programus.nxj.rockboy.core.World;
import org.programus.nxj.rockboy.core.io.IOModule;
import org.programus.nxj.rockboy.core.mc.McPoint;
import org.programus.nxj.rockboy.core.mc.McUtil;
import org.programus.nxj.rockboy.core.thing.DropCalculator;
import org.programus.nxj.rockboy.core.thing.EatBeanCalculator;
import org.programus.nxj.rockboy.core.thing.ReboundCalculator;
import org.programus.nxj.rockboy.core.thing.Thing;

public class BouncyBall extends Thing {	
	private static Image image = new Image(5, 5, new byte[]{
		(byte) 0x0e, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x0e, 
	}); 
	
	private static double ENERGY_LIMIT = World.G * (McUtil.SCREEN_BOTTOM_OFFSET + IOModule.getIOModule().getScreenBoundary().y + 20); 
	
	private DropCalculator dc; 
	private List<Rectangle> obstacleList; 
	private ReboundCalculator rc; 
	private EatBeanCalculator ebc; 
	
	public BouncyBall(Point centerPoint, List<Rectangle> obstacleList, List<Point2D.Double> beanList) {
		this.centerPoint.setLcdPoint(centerPoint.x, centerPoint.y); 
		this.obstacleList = obstacleList; 
		this.centerPoint.setStaticInLcd(true); 
		this.speed.setStaticInLcd(true); 
		this.dc = new DropCalculator(this); 
		this.rc = new ReboundCalculator(this, this.obstacleList); 
		this.ebc = new EatBeanCalculator(this, beanList); 
	}

	@Override
	public Image getImage() {
		return image; 
	}

	@Override
	public void run() {
		McPoint oldPoint = new McPoint(this.centerPoint); 
		this.moveOneStep(); 
		if (!this.rc.rebound(oldPoint)) {
			this.dc.updateDropSpeed(); 
			double v = speed.getValue(); 
			double h = this.centerPoint.getNaturalPoint().y; 
			double ek = v * v / 2; 
			double ep = World.G * h; 
			if (ek + ep > ENERGY_LIMIT) {
				v = Math.sqrt((ENERGY_LIMIT - ep) * 2) - World.G; 
				this.speed.setValue(v > 0 ? v : World.G / 1);
//				v = Math.sqrt((ENERGY_LIMIT - ep) * 2); 
//				this.speed.setValue(v);
			}
		} else {
			IOModule.getIOModule().playTone(4000, 10); 
		}
		
		if (this.ebc.eat(oldPoint)) {
			IOModule.getIOModule().playTone(1000, 50); 
		}
	}
}
