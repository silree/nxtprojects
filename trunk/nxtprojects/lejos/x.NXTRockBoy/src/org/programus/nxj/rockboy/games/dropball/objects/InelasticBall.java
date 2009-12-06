package org.programus.nxj.rockboy.games.dropball.objects;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import javax.microedition.lcdui.Image;

import org.programus.nxj.rockboy.core.mc.McPoint;
import org.programus.nxj.rockboy.core.thing.BoundaryLoopCalculator;
import org.programus.nxj.rockboy.core.thing.DropCalculator;
import org.programus.nxj.rockboy.core.thing.ObstructCalculator;
import org.programus.nxj.rockboy.core.thing.Thing;
import org.programus.nxj.util.music.BGMBox;

/**
 * This is the ball dropping. 
 * @author Programus
 *
 */
public class InelasticBall extends Thing {	
	private static Image image = new Image(5, 5, new byte[]{
		(byte) 0x0e, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x0e, 
	}); 
	
	private int radius = 2; 
	private static double SPEED_LIMIT = 5; 
	
	private double obstacleSpeed; 
	
	private DropCalculator dc; 
	private List<Rectangle> obstacleList; 
	private ObstructCalculator oc; 
	private BoundaryLoopCalculator blc; 
	
	public InelasticBall(Point centerPoint, List<Rectangle> obstacleList) {
		this.centerPoint.setLcdPoint(centerPoint.x, centerPoint.y); 
		this.obstacleList = obstacleList; 
		this.centerPoint.setStaticInLcd(true); 
		this.speed.setStaticInLcd(true); 
		this.dc = new DropCalculator(this); 
		this.oc = new ObstructCalculator(this, this.obstacleList); 
		this.blc = new BoundaryLoopCalculator(this, BoundaryLoopCalculator.X_LOOP_MARK); 
	}
	
	public int getRadius() {
		return this.radius; 
	}

	public void setObstacleSpeed(double obstacleSpeed) {
		this.obstacleSpeed = obstacleSpeed;
	}

	@Override
	public Image getImage() {
		return image; 
	}

	@Override
	public void run() {
		McPoint oldPoint = new McPoint(this.centerPoint); 
		double oldV = speed.getValue(); 
		this.moveOneStep(); 
		int obstructed = this.oc.obstruct(oldPoint, this.obstacleSpeed); 
		double v = speed.getValue(); 
		if (Math.abs(v) - Math.abs(oldV) < -0.5 || (obstructed & ObstructCalculator.Y) > 0) {
			BGMBox.getInstance().playSound(4000, 10, 100); 
		}
		
		this.dc.updateDropSpeed(); 
		v = speed.getValue(); 
		if (v > SPEED_LIMIT) {
			this.speed.setValue(SPEED_LIMIT); 
		}
		
		this.blc.loop(); 
	}
}
