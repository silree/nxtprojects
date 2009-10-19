package org.programus.nxj.rockboy.core.thing;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.List;

import org.programus.nxj.rockboy.core.mc.McPoint;
import org.programus.nxj.rockboy.core.mc.McUtil;
import org.programus.nxj.rockboy.core.mc.McVector;

public class ReboundCalculator {
	private Thing master; 
	private List<Rectangle> obstacleList; 
	
	public ReboundCalculator(Thing t, List<Rectangle> obstacleList) {
		this.master = t; 
		this.obstacleList = obstacleList; 
	}
	
	public boolean rebound(McPoint oldPoint) {
		boolean rebounded = false; 
		if (oldPoint == null) {
			return rebounded; 
		}
		McVector speed = master.speed; 
		McPoint point = master.centerPoint; 
		Point2D.Double op = oldPoint.getLcdPoint(); 
		Point2D.Double p = point.getLcdPoint(); 
		if (this.obstacleList != null) {
			for (Rectangle obstacle : this.obstacleList) {
				boolean reverseX = false; 
				int baseY = 0; 
				boolean reverseY = false; 
				int baseX = 0; 
				
				int outOp = obstacle.outcode(op); 
				int outP = obstacle.outcode(p); 
				if ((outOp & outP) != 0) {
					// object didn't touch the obstacle. 
					continue; 
				}
				if ((outOp & Rectangle.OUT_TOP) > 0) {
					baseY = obstacle.y; 
				}
				if ((outOp & Rectangle.OUT_BOTTOM) > 0) {
					baseY = obstacle.y + obstacle.height; 
				}
				if ((outOp & Rectangle.OUT_LEFT) > 0) {
					baseX = obstacle.x; 
				}
				if ((outOp & Rectangle.OUT_RIGHT) > 0) {
					baseX = obstacle.x + obstacle.width; 
				}
				switch(outOp) {
				case Rectangle.OUT_TOP:
				case Rectangle.OUT_BOTTOM:
					reverseX = true; 
					break; 
				case Rectangle.OUT_LEFT:
				case Rectangle.OUT_RIGHT:
					reverseY = true; 
					break; 
				case Rectangle.OUT_TOP | Rectangle.OUT_LEFT:
					{
						double tgpp = Math.abs(p.y - op.y) / Math.abs(p.x - op.x); 
						double tgpr = Math.abs(obstacle.y - op.y) / Math.abs(obstacle.x - op.x); 
						if (tgpp <= tgpr) {
							reverseX = true; 
						}
						if (tgpp >= tgpr) {
							reverseY = true; 
						}
					}
					break; 
				case Rectangle.OUT_TOP | Rectangle.OUT_RIGHT:
					{
						double tgpp = Math.abs(p.y - op.y) / Math.abs(p.x - op.x); 
						double tgpr = Math.abs(obstacle.y - op.y) / Math.abs(obstacle.x + obstacle.width - op.x); 
						if (tgpp <= tgpr) {
							reverseX = true; 
						}
						if (tgpp >= tgpr) {
							reverseY = true; 
						}
					}
					break; 
				case Rectangle.OUT_BOTTOM | Rectangle.OUT_LEFT:
					{
						double tgpp = Math.abs(p.y - op.y) / Math.abs(p.x - op.x); 
						double tgpr = Math.abs(obstacle.y + obstacle.height - op.y) / Math.abs(obstacle.x - op.x); 
						if (tgpp <= tgpr) {
							reverseX = true; 
						}
						if (tgpp >= tgpr) {
							reverseY = true; 
						}
					}
					break; 
				case Rectangle.OUT_BOTTOM | Rectangle.OUT_RIGHT:
					{
						double tgpp = Math.abs(p.y - op.y) / Math.abs(p.x - op.x); 
						double tgpr = Math.abs(obstacle.y + obstacle.height - op.y) / Math.abs(obstacle.x + obstacle.width - op.x); 
						if (tgpp <= tgpr) {
							reverseX = true; 
						}
						if (tgpp >= tgpr) {
							reverseY = true; 
						}
					}
					break; 
				}
				
				
				if (reverseX) {
					this.reverseX(speed, point, p, baseY); 
					rebounded = true; 
				}
				if (reverseY) {
					this.reverseY(speed, point, p, baseX); 
					rebounded = true; 
				}
			}
		}
		McUtil util = McUtil.getInstance(); 
		Point bound = util.getLcdCooridnateOffset(); 
		
		if (p.y <= 0) {
			this.reverseX(speed, point, p, 0); 
			rebounded = true; 
		} else if (p.y >= bound.y) {
			this.reverseX(speed, point, p, bound.y); 
			rebounded = true; 
		}
		if (p.x <= 0) {
			this.reverseY(speed, point, p, 0); 
			rebounded = true; 
		} else if (p.x >= bound.x) {
			this.reverseY(speed, point, p, bound.x); 
			rebounded = true; 
		}
		
		return rebounded; 
	}
	
	private void reverseX(McVector speed, McPoint point, Point2D.Double p, int baseY) {
		double radian = speed.getMcAngleRef().getLcdRadian(); 
		speed.getMcAngleRef().setLcdRadian(-radian); 
		speed.getMcAngleRef().ltRound(); 
		p.y = (baseY << 1) - p.y; 
		point.setLcdPoint(p); 
	}
	
	private void reverseY(McVector speed, McPoint point, Point2D.Double p, int baseX) {
		double radian = speed.getMcAngleRef().getLcdRadian(); 
		speed.getMcAngleRef().setLcdRadian(Math.PI - radian); 
		speed.getMcAngleRef().ltRound(); 
		p.x = (baseX << 1) - p.x; 
		point.setLcdPoint(p); 
	}
}