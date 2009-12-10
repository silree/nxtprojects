package org.programus.nxj.rockboy.core.thing;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.List;

import org.programus.nxj.rockboy.core.mc.McPoint;
import org.programus.nxj.rockboy.core.mc.McVector;

/**
 * This is a class to process the obstruct action. 
 * @author Programus
 *
 */
public class ObstructCalculator {
	private Thing master; 
	private List<Rectangle> obstacleList; 
	
	public final static int X = 1; 
	public final static int Y = 1 << 1; 
	
	public ObstructCalculator(Thing t, List<Rectangle> obstacleList) {
		this.master = t; 
		this.obstacleList = obstacleList; 
	}
	
	public int obstruct(McPoint oldPoint, double obstacleSpeed) {
		int obstructed = 0; 
		if (oldPoint == null) {
			return obstructed; 
		}
		
		McVector speed = master.speed; 
		McPoint point = master.centerPoint; 
		// op means old point
		Point2D.Double op = oldPoint.getLcdPoint(); 
		// p means current point
		Point2D.Double p = point.getLcdPoint(); 
//		RConsole.println("op:" + op); 
//		RConsole.println("p:" + p); 
		if (this.obstacleList != null) {
			// Check all obstacles one by one. 
			for (Rectangle obstacle : this.obstacleList) {
				boolean obstructX = false; 
				int baseY = 0; 
				boolean obstructY = false; 
				int baseX = 0; 
				
				int outOp = obstacle.outcode(op); 
				int outP = obstacle.outcode(p); 
//				RConsole.println("o:" + obstacle); 
//				RConsole.println("outOp" + outOp); 
//				RConsole.println("outP:" + outP); 
				if ((outOp & outP) != 0) {
					// old point and current point r at the same side of the obstacle, 
					// which means object didn't touch the obstacle. 
					continue; 
				}
				
				if (outOp == 0 && outP == 0) {
					// Dropped from above very quickly. 
					baseY = obstacle.y; 
					obstructX = true; 
				}
				if ((outOp & Rectangle.OUT_TOP) > 0) {
					// old point is on the top of the obstacle, 
					// if there is a rebound, 
					// must occur on the top side of the obstacle.  
					baseY = obstacle.y; 
				}
				if ((outOp & Rectangle.OUT_BOTTOM) > 0) {
					// old point is on the bottom of the obstacle, 
					// if there is a rebound, 
					// must occur on the bottom side of the obstacle.  
					baseY = obstacle.y + obstacle.height; 
				}
				if ((outOp & Rectangle.OUT_LEFT) > 0) {
					// old point is on the left of the obstacle, 
					// if there is a rebound, 
					// must occur on the left side of the obstacle.  
					baseX = obstacle.x; 
				}
				if ((outOp & Rectangle.OUT_RIGHT) > 0) {
					// old point is on the right of the obstacle, 
					// if there is a rebound, 
					// must occur on the right side of the obstacle.  
					baseX = obstacle.x + obstacle.width; 
				}
				switch(outOp) {
				case Rectangle.OUT_TOP:
				case Rectangle.OUT_BOTTOM:
					// if the old point is on the top side or bottom side, 
					// the rebound will occur on horizontal. 
					obstructX = true; 
					break; 
				case Rectangle.OUT_LEFT:
				case Rectangle.OUT_RIGHT:
					// if the old point is on the left side or right side, 
					// the rebound will occur on vertical. 
					obstructY = true; 
					break; 
				case Rectangle.OUT_TOP | Rectangle.OUT_LEFT:
					{
						// top-left side, 
						// need some angle calculation
						double tgpp = Math.abs(p.y - op.y) / Math.abs(p.x - op.x); 
						double tgpr = Math.abs(obstacle.y - op.y) / Math.abs(obstacle.x - op.x); 
						if (tgpp <= tgpr) {
							obstructX = true; 
						}
						if (tgpp >= tgpr) {
							obstructY = true; 
						}
					}
					break; 
				case Rectangle.OUT_TOP | Rectangle.OUT_RIGHT:
					{
						// top-right side, 
						// need some angle calculation
						double tgpp = Math.abs(p.y - op.y) / Math.abs(p.x - op.x); 
						double tgpr = Math.abs(obstacle.y - op.y) / Math.abs(obstacle.x + obstacle.width - op.x); 
						if (tgpp <= tgpr) {
							obstructX = true; 
						}
						if (tgpp >= tgpr) {
							obstructY = true; 
						}
					}
					break; 
				case Rectangle.OUT_BOTTOM | Rectangle.OUT_LEFT:
					{
						// bottom-left side, 
						// need some angle calculation
						double tgpp = Math.abs(p.y - op.y) / Math.abs(p.x - op.x); 
						double tgpr = Math.abs(obstacle.y + obstacle.height - op.y) / Math.abs(obstacle.x - op.x); 
						if (tgpp <= tgpr) {
							obstructX = true; 
						}
						if (tgpp >= tgpr) {
							obstructY = true; 
						}
					}
					break; 
				case Rectangle.OUT_BOTTOM | Rectangle.OUT_RIGHT:
					{
						// bottom-right side, 
						// need some angle calculation
						double tgpp = Math.abs(p.y - op.y) / Math.abs(p.x - op.x); 
						double tgpr = Math.abs(obstacle.y + obstacle.height - op.y) / Math.abs(obstacle.x + obstacle.width - op.x); 
						if (tgpp <= tgpr) {
							obstructX = true; 
						}
						if (tgpp >= tgpr) {
							obstructY = true; 
						}
					}
					break; 
				}
				
				if (obstructX) {
					this.obstructX(speed, point, p, baseY, obstacleSpeed); 
					obstructed |= X; 
				}
				if (obstructY) {
					this.obstructY(speed, point, p, baseX); 
					obstructed |= Y; 
				}
				
				if (obstructed > 0) {
					break; 
				}
			}
		}
				
		return obstructed; 
	}
	
	private void obstructX(McVector speed, McPoint point, Point2D.Double p, int baseY, double obstacleSpeed) {
//		RConsole.println("obstructX"); 
//		RConsole.println("speed:" + speed); 
//		RConsole.println("p:" + point); 
		Point2D.Double lcdSpeed = speed.getLcdOrthValues(); 
		p.y = baseY - 0.0001; 
		lcdSpeed.y = -obstacleSpeed; 
		speed.setLcdOrthValues(lcdSpeed); 
		point.setLcdPoint(p); 
//		RConsole.println("speed:" + speed); 
//		RConsole.println("p:" + point); 
//		RConsole.println("obstructX over"); 
	}
	
	private void obstructY(McVector speed, McPoint point, Point2D.Double p, int baseX) {
//		RConsole.println("obstructY"); 
//		RConsole.println("speed:" + speed); 
//		RConsole.println("p:" + point); 
//		Point2D.Double lcdSpeed = speed.getLcdOrthValues(); 
//		p.x = baseX - Math.signum(lcdSpeed.x) * .4; 
//		lcdSpeed.x = 0; 
//		speed.setLcdOrthValues(lcdSpeed); 
//		point.setLcdPoint(p); 
		// rebound
		double radian = speed.getMcAngleRef().getLcdRadian(); 
		speed.getMcAngleRef().setLcdRadian(Math.PI - radian); 
		speed.getMcAngleRef().ltRound(); 
		p.x = (baseX << 1) - p.x; 
		point.setLcdPoint(p); 
//		RConsole.println("speed:" + speed); 
//		RConsole.println("p:" + point); 
//		RConsole.println("obstructY over"); 
	}
}
