package org.programus.nxj.rockboy.games.dropball.ctrls;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;

import org.programus.nxj.rockboy.core.io.IOModule;
import org.programus.nxj.rockboy.core.mc.McUtil;
import org.programus.nxj.rockboy.games.bball.ctrls.KeyStopCondition;
import org.programus.nxj.rockboy.games.dropball.objects.InelasticBall;
import org.programus.nxj.util.Condition;
import org.programus.nxj.util.DisplayUtil;
import org.programus.nxj.util.MathUtil;
import org.programus.nxj.util.music.BGMBox;
import org.programus.nxj.util.music.Music;
import org.programus.nxj.util.music.SheetParam;
import org.programus.nxj.util.txtimg.TextImage;
import org.programus.nxj.util.txtimg.TextImage3x5;

/**
 * An object of this class represent a single level of BBall game. 
 * @author Programus
 *
 */
public class GameLevel {
	private Point initPosition; 
	private List<Rectangle> obstacleList;
	private Random rand; 
	
	private DropBallGame game; 
	private int gameValue; 
	private long startTime; 
	
	public final static Condition DEFAULT_STOP_CONDITION = new KeyStopCondition(Button.ESCAPE); 	
	
	private final static IOModule IO = IOModule.getIOModule(); 
	private final static int W = IO.getScreenBoundary().x; 
	private final static int H = IO.getScreenBoundary().y; 
	
	private final static int TIME_PER_FRAME = 30; 
	
	private final static int FIRST_SPACE = 30; 
	private final static int HEIGHT = 13; 
	private final static int MIN_STEP = HEIGHT + 2; 
	private final static int MAX_STEP = H * 2 / 3; 
	
	private final static int MIN_FIXED_WIDTH = 10; 
	private final static int MAX_FIXED_WIDTH = W - 12; 
	
	private double speed; 
	private double offset; 
	private int turnCounter; 
	
	private final static double INIT_SPEED = .12; 
	private final static double LIMIT_SPEED = 10; 
	private final static int SPEED_UP_TURN = 10 * 1000 / TIME_PER_FRAME; 
	private final static double SPEED_UP_STEP = .01; 
	
	private final static TextImage TEXT_IMAGE = new TextImage3x5(); 
	
	GameLevel(DropBallGame game) {
		this.initPosition = new Point();  
		this.obstacleList = new ArrayList<Rectangle>(); 
		this.gameValue = 0; 
		this.rand = new Random(); 
		this.game = game; 
	}
	
	public void initialize() {
		this.obstacleList.clear(); 
		for (int i = FIRST_SPACE; i < FIRST_SPACE + H + MAX_STEP; i += this.getStep()) {
			Rectangle r = new Rectangle(0, i, 0, HEIGHT); 
			this.randomObstacleX(r); 
//			RConsole.println("r1:" + r); 
			this.obstacleList.add(r); 
			r = new Rectangle(r.x - W, r.y, r.width, r.height); 
//			RConsole.println("r2:" + r); 
			this.obstacleList.add(r); 
		}
		
		Rectangle firstObstacle = this.obstacleList.get(0); 
		
		this.initPosition.x = firstObstacle.x + this.rand.nextInt(firstObstacle.width); 
		this.initPosition.y = this.rand.nextInt(FIRST_SPACE); 
		
		this.gameValue = 0; 
		
		this.speed = INIT_SPEED; 
		this.turnCounter = 0; 
	}
	
	public Point getInitPosition() {
		return initPosition;
	}
	
	public List<Rectangle> getObstacleList() {
		return obstacleList;
	}
	
	public int getGameValue() {
		return this.gameValue; 
	}
	
	private int getStep() {
		int lightValue = this.game.isLightStepMode() ? IO.getRawLightValue() : 768; 
		int v = (MAX_STEP - MIN_STEP) * (1023 - lightValue) / 1023; 
		return MIN_STEP + (v < 0 ? 0 : v); 
	}

	private boolean isOver(Point p, int r) {
		if (p.y < r - HEIGHT) {
			return true; 
		} else if (p.y > IO.getScreenBoundary().y + r) {
			this.gameValue += (this.obstacleList.size() >> 1); 
			return true; 
		}
		return false; 
	}
	
	private int getObstacleMoveOffset() {
		this.offset += this.speed; 
		this.turnCounter++; 
		
		int ret = (int) MathUtil.round(this.offset); 
		this.offset -= ret; 
		
		return (int) ret; 
	}
	
	private void speedUp() {
		if (this.turnCounter % SPEED_UP_TURN == 0) {
			this.speed += SPEED_UP_STEP; 
			if (this.speed > LIMIT_SPEED) {
				this.speed = LIMIT_SPEED; 
			}
		}
	}
	
	private void resetCounter() {
		int commonMultiple = SPEED_UP_TURN; 
		if (this.turnCounter > commonMultiple && this.turnCounter % commonMultiple == 0) {
			this.turnCounter = 1; 
		}
	}
	
	private void randomObstacleX(Rectangle obstacle) {
		int lightValue = this.game.isLightWidthMode() ? IO.getRawLightValue() : 512; 
		int fixedWidth = MIN_FIXED_WIDTH + (MAX_FIXED_WIDTH - MIN_FIXED_WIDTH) * lightValue / 1023; 
		int randomRange = (W - fixedWidth) >> 2; 
		int space = 0; 
		obstacle.width = this.rand.nextInt(randomRange) + fixedWidth - space; 
		obstacle.x = this.rand.nextInt(W); 
		obstacle.height = HEIGHT; 
	}
	
	private boolean generateNewObstacles(int i) {
		Rectangle obstacle = this.obstacleList.get(i);
		if (obstacle.y < -obstacle.height && (i % 2) > 0) {
			int prevIndex = (i + this.obstacleList.size() - 2) % this.obstacleList.size(); 
			obstacle.y = this.obstacleList.get(prevIndex).y + this.getStep(); 
			this.randomObstacleX(obstacle); 
			
			Rectangle pairObstacle = this.obstacleList.get(i - 1); 
			pairObstacle.y = obstacle.y; 
			pairObstacle.width = obstacle.width; 
			pairObstacle.x = obstacle.x - W; 
			
			return true; 
		}
		
		return false; 
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

		// To prevent the button event
		Delay.msDelay(200); 
		
		this.startTime = System.currentTimeMillis(); 
		
		InelasticBall ball = new InelasticBall(initPosition, obstacleList); 
		
		boolean over = false; 
		
		if (stopCondition == null) {
			stopCondition = DEFAULT_STOP_CONDITION; 
		}
		
		int markIndex = -10; 
		Image markNum = null; 
		int markFactor = 10; 
		
		BGMBox box = BGMBox.getInstance(); 
		box.playLoop(1000); 
		while (!stopCondition.isSatisfied()) {
			long calcStartTime = System.currentTimeMillis(); 
			if (pauseCondition != null && pauseCondition.isSatisfied()) {
				// pause. 
				long t = System.currentTimeMillis(); 
				LCD.clear(); 
				LCD.drawString("PAUSE", 5, 3); 
				LCD.refresh(); 
				Delay.msDelay(TIME_PER_FRAME); 
				this.startTime += System.currentTimeMillis() - t; 
				continue; 
			}
			
			util.updateAngle(); 
			
			// calculate obstacles
			int offset = this.getObstacleMoveOffset(); 
			this.speedUp(); 
			this.resetCounter(); 
			for (int i = 0; i < this.obstacleList.size(); i++) {
				Rectangle obstacle = this.obstacleList.get(i);
				obstacle.y -= offset; 
				if (this.generateNewObstacles(i)) {
					this.gameValue++; 
					int n = (int) (this.gameValue + (this.obstacleList.size() >> 1)); 
					if (n % markFactor == 0) {
						markNum = TEXT_IMAGE.getImage(n); 
						markIndex = i; 
					} else if (i == markIndex) {
						markIndex = -10; 
					}
				}
			}
			// calculate ball --------------------------------
			ball.setObstacleSpeed(this.speed); 
			ball.run(); 
			Point ballDrawPoint = ball.getTopLeft(); 
			// For debug
//			RConsole.println(String.valueOf(ballDrawPoint)); 
//			RConsole.println("=========================================="); 
			// --------------------------------------------------
			
			// clear screen.
			g.clear(); 
			
			// Paint screen =====================================
			DisplayUtil.drawImageCross(ball.getImage(), ballDrawPoint.x, ballDrawPoint.y, DisplayUtil.X_FLAG, LCD.ROP_OR); 
			for (int i = 0; i < this.obstacleList.size(); i++) {
				Rectangle obstacle = this.obstacleList.get(i); 
				int gap = ball.getRadius() + 1; 
				g.fillRect(obstacle.x, obstacle.y + gap, obstacle.width, obstacle.height - (gap << 1)); 
				if (i == markIndex || i == markIndex - 1) {
					int color = g.getColor(); 
					g.setColor(Graphics.WHITE); 
					gap++; 
					g.fillRect(obstacle.x + 1, obstacle.y + gap, obstacle.width - 2, obstacle.height - (gap << 1)); 
					g.setColor(color); 
					g.drawImage(markNum, 0, 0, obstacle.x + ((obstacle.width - markNum.getWidth()) >> 1), obstacle.y + gap, markNum.getWidth(), markNum.getHeight(), LCD.ROP_XOR); 
				}
			}
			g.refresh(); 
			
			if (this.isOver(ballDrawPoint, ball.getRadius() + 1)) {
				over = true; 
				break; 
			}
			// ==================================================
			int calcTime = (int)(System.currentTimeMillis() - calcStartTime); 
			int delayTime = TIME_PER_FRAME - calcTime; 
			if (delayTime > 0) {
				Delay.msDelay(delayTime); 
			} else {
//				// Just for debug. 
//				Sound.playTone(2000, 100); 
			}
		}
		
		box.stop(); 
		
		if (over) {
			// draw over animation
			final int LIMIT = (g.getHeight() >> 1) + 1; 
			new Thread(new Runnable() {
				@Override
				public void run() {
					new Music(new SheetParam(SheetParam.FAST_BEAT_DUR, 0, new int[]{20, 0, 10, 0}, false), new String[] {
						"6b/16 6a#/16 6a/16 6g#16 6g/16 6f#/16 6f/16 6e/16 6d#/16 6d/16 6c#/16 6c/16", 
						"5b/16 5a#/16 5a/16 5g#16 5g/16 5f#/16 ", 
						"4g/2", 
					}).play(Sound.PIANO); 
				}
			}).start(); 

			for (int topLine = 0; !stopCondition.isSatisfied() && topLine < LIMIT; topLine++) {
				g.fillRect(0, 0, g.getWidth(), topLine); 
				g.fillRect(0, g.getHeight() - topLine - 1, g.getWidth(), topLine + 1); 
				g.refresh(); 
				Delay.msDelay(30); 
			}
			DisplayUtil.drawStringCenter("GAME OVER", 3 * LCD.CELL_HEIGHT, true); 
			g.refresh(); 
			Delay.msDelay(3000); 
		}
		
		return over; 
	}
}
