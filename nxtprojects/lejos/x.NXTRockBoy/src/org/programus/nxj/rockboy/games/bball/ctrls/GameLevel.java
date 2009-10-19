package org.programus.nxj.rockboy.games.bball.ctrls;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.lcdui.Graphics;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.Delay;

import org.programus.nxj.rockboy.core.mc.McUtil;
import org.programus.nxj.rockboy.games.bball.objects.BouncyBall;
import org.programus.nxj.util.Condition;

public class GameLevel {
	private String title; 
	private Point initPosition; 
	private List<Rectangle> obstacleList;
	
	public final static Condition DEFAULT_STOP_CONDITION = new KeyStopCondition(Button.ESCAPE); 
	private final static int TITLE_TIME = 2000; 
	
	GameLevel() {
		this.initPosition = new Point();  
		this.obstacleList = new ArrayList<Rectangle>(); 
	}
	
	protected void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Point getInitPosition() {
		return initPosition;
	}
	
	public List<Rectangle> getObstacleList() {
		return obstacleList;
	}
	
	private Point2D.Double generateBean(Random rand, Graphics g) {
		if (rand == null) {
			rand = new Random(); 
		}
		Point2D.Double p = new Point2D.Double(); 
		boolean availablePoint = false; 
		while (!availablePoint) {
			p.setLocation(rand.nextInt(g.getWidth()), rand.nextInt(g.getHeight())); 
			availablePoint = true; 
			for (Rectangle rect : obstacleList) {
				if (rect.contains(p)) {
					availablePoint = false; 
					break; 
				}
			}
		}
		
		return p; 
	}
	
	private List<Point2D.Double> generateRandomBeans(Graphics g) {
		Random rand = new Random(); 
		List<Point2D.Double> beans = new ArrayList<Point2D.Double>(); 
		for (int i = 0; i < 5; i++) {
			beans.add(this.generateBean(rand, g)); 
		}
		return beans; 
	}
	
	private void showTitle() {
		LCD.clear(); 
		int x = (LCD.DISPLAY_CHAR_WIDTH - this.getTitle().length()) >> 1; 
		LCD.drawString(this.getTitle(), x, 3); 
		LCD.refresh(); 
	}
	
	/**
	 * Play this level. 
	 * @param stopCondition
	 * @param pauseCondition
	 * @return true if this level is passed, and false if it is stopped. 
	 */
	public boolean play(Condition stopCondition, Condition pauseCondition) {
		McUtil util = McUtil.getInstance(); 
		Graphics g = new Graphics(); 
		g.autoRefresh(false); 
		
		this.showTitle(); 
		long startTime = System.currentTimeMillis(); 
		
		List<Point2D.Double> beans = this.generateRandomBeans(g); 

		BouncyBall ball = new BouncyBall(this.getInitPosition(), this.obstacleList, beans); 
		
		boolean ret = false; 
		
		if (stopCondition == null) {
			stopCondition = DEFAULT_STOP_CONDITION; 
		}
		
		
		while (!stopCondition.isSatisfied()) {
			if (System.currentTimeMillis() < startTime + TITLE_TIME) {
				// displaying level title
				continue; 
			}
			if (pauseCondition != null && pauseCondition.isSatisfied()) {
				// pause. 
				LCD.clear(); 
				LCD.drawString("PAUSE", 5, 3); 
				LCD.refresh(); 
				continue; 
			}
			
			util.updateAngle(); 
			ball.run(); 
//			g.drawString("angle: " + input.getRotationAngle() + "   ", 0, 0); 
			
			Point ballDrawPoint = ball.getTopLeft(); 
			Point2D.Double beanP = null; ; 
			if (beans.size() > 0) {
				beanP = beans.get(beans.size() - 1); 
			} else {
				ret = true; 
				break; 
			}
			
			// Paint screen.
			g.clear(); 
			g.drawImage(ball.getImage(), ballDrawPoint.x, ballDrawPoint.y, false); 
			if (beanP != null) {
				g.fillArc((int)beanP.x - 1, (int)beanP.y - 1, 3, 3, 0, 360); 
			}
			for (Rectangle r : obstacleList) {
				g.fillRect(r.x < 0 ? 0 : r.x, 
						r.y < 0 ? 0 : r.y, 
						r.width > g.getWidth() ? g.getWidth() : r.width, 
						r.height > g.getHeight() ? g.getHeight() : r.height); 
			}
			g.refresh(); 
			Delay.msDelay(20); 
		}
		return ret; 
	}
}
