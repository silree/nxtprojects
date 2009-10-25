package org.programus.nxj.rockboy.games.bball.ctrls;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;

import org.programus.nxj.rockboy.core.mc.McUtil;
import org.programus.nxj.rockboy.games.bball.objects.BouncyBall;
import org.programus.nxj.util.Condition;
import org.programus.nxj.util.DisplayUtil;

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
	
	private final static int TIME_PER_FRAME = 30; 
	
	private final static Image BEAN_IMAGE = new Image(3, 3, new byte[] {
			0x02, 0x07, 0x02, 
	}); 
	
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
			p.setLocation(rand.nextInt(g.getWidth() - 2) + 1, rand.nextInt(g.getHeight() - 2) + 1); 
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
		DisplayUtil.drawStringCenter(this.getTitle(), 3); 
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
			int w = g.getWidth(); 
			int len = (beans.size()) * g.getWidth() / BEAN_LIMIT; 
			int xy = g.getHeight() - 1; 
			if (len > 0) {
				g.drawLine(w, xy, w - len, xy); 
			}
			// ms time status
			long t = this.gameValue % 1000; 
			len = (int)(t * g.getWidth() / 1000); 
			xy = 0; 
			if (len > 0) {
				g.drawLine(0, xy, len, xy); 
			}
			// sec time status
			int h = g.getHeight(); 
			t = (this.gameValue - t) % 60000; 
			len = (int)(t * g.getHeight() / 60000); 
			xy = g.getWidth() - 1; 
			if (len > 0) {
				g.drawLine(xy, h, xy, h - len); 
			}
			// min time status
			t = (this.gameValue - t) % 3600000; 
			len = (int)(t * g.getHeight() / 3600000); 
			xy = 0; 
			if (len > 0) {
				g.drawLine(xy, h, xy, h - len); 
			}
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
			int w = g.getWidth(); 
			if (len > 0) {
				g.drawLine(w, xy, len, xy); 
			} else {
				Sound.beep(); 
			}
		}
	}
	
	private Image getObstacleImage(Graphics g) {
		// don't worry about clear and redraw, because screen won't be updated since there is no refresh. 
		g.clear(); 
		for (Rectangle r : this.obstacleList) {
			g.fillRect(r.x < 0 ? 0 : r.x, 
					r.y < 0 ? 0 : r.y, 
					r.width > g.getWidth() ? g.getWidth() : r.width, 
					r.height > g.getHeight() ? g.getHeight() : r.height); 
		}
		byte[] display = LCD.getDisplay(); 
		byte[] data = new byte[display.length]; 
		System.arraycopy(display, 0, data, 0, display.length); 
		return new Image(LCD.SCREEN_WIDTH, LCD.SCREEN_HEIGHT, data); 
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
		
		// calculate the image of all obstacles. 
		Image obstacleImage = this.getObstacleImage(g); 
		
		while (!stopCondition.isSatisfied()) {
			long calcStartTime = System.currentTimeMillis(); 
			if (calcStartTime < this.startTime) {
				// displaying level title
				this.showTitle(); 
				Delay.msDelay(TIME_PER_FRAME); 
				continue; 
			}
			if (pauseCondition != null && pauseCondition.isSatisfied()) {
				// pause. 
				LCD.clear(); 
				LCD.drawString("PAUSE", 5, 3); 
				LCD.refresh(); 
				Delay.msDelay(TIME_PER_FRAME); 
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
			// draw obstacles
			if (this.obstacleList != null && this.obstacleList.size() > 0) {
				g.drawImage(obstacleImage, 0, 0, 0, 0, obstacleImage.getWidth(), obstacleImage.getHeight(), LCD.ROP_OR); 
			}
			// draw bean
			if (beanP != null) {
				g.drawImage(BEAN_IMAGE, 0, 0, (int)beanP.x - 1, (int)beanP.y - 1, BEAN_IMAGE.getWidth(), BEAN_IMAGE.getHeight(), LCD.ROP_OR); 
//				g.fillArc((int)beanP.x - 1, (int)beanP.y - 1, 3, 3, 0, 360); 
			}
			// draw ball
//			g.drawImage(ball.getImage(), ballDrawPoint.x, ballDrawPoint.y, false); 
			g.drawImage(ball.getImage(), 0, 0, ballDrawPoint.x, ballDrawPoint.y, ball.getWidth(), ball.getHeight(), LCD.ROP_OR); 
			this.drawStatus(beans, g); 
			g.refresh(); 
			// ==================================================
			int calcTime = (int)(System.currentTimeMillis() - calcStartTime); 
			int delayTime = TIME_PER_FRAME - calcTime; 
			if (delayTime > 0) {
				Delay.msDelay(delayTime); 
			}
		}
		
		if (win) {
			// draw over animation
			final int LIMIT = (g.getHeight() >> 1) + 1; 
			for (int topLine = 0; !stopCondition.isSatisfied() && topLine < LIMIT; topLine++) {
				g.fillRect(0, 0, g.getWidth(), topLine); 
				g.fillRect(0, g.getHeight() - topLine - 1, g.getWidth(), topLine + 1); 
				g.refresh(); 
				Delay.msDelay(20); 
			}
		}
		
		return win; 
	}
}
