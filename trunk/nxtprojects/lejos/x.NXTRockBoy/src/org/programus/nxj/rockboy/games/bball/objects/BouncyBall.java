package org.programus.nxj.rockboy.games.bball.objects;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.microedition.lcdui.Image;

import org.programus.nxj.rockboy.core.DropCalculator;
import org.programus.nxj.rockboy.core.ReboundCalculator;
import org.programus.nxj.rockboy.core.Thing;
import org.programus.nxj.rockboy.core.mc.McPoint;

public class BouncyBall extends Thing {	
	private static Image image = new Image(5, 5, new byte[]{
		(byte) 0x0e, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x0e, 
	}); 
	
	private static double SPEED_LIMIT = 10; 
	
	private DropCalculator dc; 
	private List<Rectangle2D.Double> obstacleList; 
	private ReboundCalculator rc; 
	
	public BouncyBall(Point centerPoint, List<Rectangle2D.Double> obstacleList) {
		this.centerPoint.setLcdPoint(centerPoint.x, centerPoint.y); 
		this.obstacleList = obstacleList; 
		this.centerPoint.setStaticInLcd(false); 
		this.speed.setStaticInLcd(false); 
		this.dc = new DropCalculator(this); 
		this.rc = new ReboundCalculator(this, this.obstacleList); 
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
			if (this.speed.getValue() > SPEED_LIMIT) {
				this.speed.setValue(SPEED_LIMIT); 
			}
		}
	}

}
