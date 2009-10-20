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
	private BBGame game; 
	private String title; 
	private Point initPosition; 
	private List<Rectangle> obstacleList;
	private Random rand; 
	
	private long gameValue; 
	private long startTime; 
	
	public final static Condition DEFAULT_STOP_CONDITION = new KeyStopCondition(Button.ESCAPE); 
	private final static int TITLE_TIME = 2000; 
	private final static int BEAN_LIMIT = 5; 
	private final static int TIME_LIMIT = 60000; 
	
	GameLevel(BBGame game) {
		this.game = game; 
		this.initPosition = new Point();  
		this.obstacleList = new ArrayList<Rectangle>(); 
		this.gameValue = 0; 
		this.rand = new Random(); 
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
	
	public long getGameValue() {
		return this.gameValue; 
	}
	
	private Point2D.Double generateBean(Graphics g) {
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
	
	private List<Point2D.Double> generateRandomBeans(Graphics g, int n) {
		List<Point2D.Double> beans = new ArrayList<Point2D.Double>(); 
		for (int i = 0; i < n; i++) {
			beans.add(this.generateBean(g)); 
		}
		return beans; 
	}
	
	private void showTitle() {
		LCD.clear(); 
		int x = (LCD.DISPLAY_CHAR_WIDTH - this.getTitle().length()) >> 1; 
		LCD.drawString(this.getTitle(), x, 3); 
		LCD.refresh(); 
	}
	
	private void updateTimeValue() {
		if (this.game.isTimeMode()) {
			this.gameValue = System.currentTimeMillis() - this.startTime; 
		}
	}
	
	private void updateScoreValue(List<Point2D.Double> beans, Graphics g) {
		if (this.game.isScoreMode()) {
			if (beans.size() <= 0) {
				this.gameValue += 1; 
				beans.add(this.generateBean(g)); 
			}
		}
	}
	
	private boolean isGoal(List<Point2D.Double> beans) {
		if (this.game.isTimeMode()) {
			return beans.size() <= 0; 
		}
		if (this.game.isScoreMode()) {
			return System.currentTimeMillis() - this.startTime >= TIME_LIMIT; 
		}
		
		return false; 
	}
	
	private void drawStatus(List<Point2D.Double> beans, Graphics g) {
		if (this.game.isTimeMode()) {
			// bean progress status
			int w = g.getWidth() - 1; 
			int len = (beans.size()) * g.getWidth() / BEAN_LIMIT; 
			int xy = g.getHeight() - 1; 
			g.drawLine(w, xy, w - len, xy); 
			// ms time status
			long t = this.gameValue % 1000; 
			len = (int)(t * g.getWidth() / 1000); 
			xy = 0; 
			g.drawLine(0, xy, len, xy); 
			// sec time status
			int h = g.getHeight() - 1; 
			t = (this.gameValue - t) % 60000; 
			len = (int)(t * g.getHeight() / 60000); 
			xy = g.getWidth() - 1; 
			g.drawLine(xy, h, xy, h - len); 
			// min time status
			t = (this.gameValue - t) % 3600000; 
			len = (int)(t * g.getHeight() / 3600000); 
			xy = 0; 
			g.drawLine(xy, h, xy, h - len); 
		}
		
		if (this.game.isScoreMode()) {
			// score status
			int len = (int) this.gameValue % g.getWidth(); 
			int xy = 0; 
			if (len > 0) {
				g.drawLine(0, xy, len, xy); 
			}
			// time status
			len = (int) (System.currentTimeMillis() - this.startTime) * g.getWidth() / TIME_LIMIT; 
			xy = g.getHeight() - 1; 
			int w = g.getWidth() - 1; 
			g.drawLine(w, xy, len, xy); 
		}
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
		long titleStartTime = System.currentTimeMillis(); 
		this.startTime = titleStartTime + TITLE_TIME; 
		
		List<Point2D.Double> beans = this.generateRandomBeans(g, this.game.isTimeMode() ? BEAN_LIMIT : 1); 

		BouncyBall ball = new BouncyBall(this.getInitPosition(), this.obstacleList, beans); 
		
		boolean win = false; 
		
		if (stopCondition == null) {
			stopCondition = DEFAULT_STOP_CONDITION; 
		}
		
		
		while (!stopCondition.isSatisfied()) {
			if (System.currentTimeMillis() < this.startTime) {
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
			
			// calculate images --------------------------------
			util.updateAngle(); 
			ball.run(); 
			this.updateScoreValue(beans, g); 
			this.updateTimeValue(); 
			
			if (this.isGoal(beans)) {
				win = true; 
				break; 
			}
			
			Point ballDrawPoint = ball.getTopLeft(); 
			Point2D.Double beanP = null; ; 
			if (beans.size() > 0) {
				beanP = beans.get(beans.size() - 1); 
			}
			// --------------------------------------------------
			
			// Paint screen =====================================
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
			
			this.drawStatus(beans, g); 
			g.refresh(); 
			// ==================================================
			Delay.msDelay(20); 
		}
		return win; 
	}
}
