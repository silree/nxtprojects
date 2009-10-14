package org.programus.nxj.rockboy.games.bball;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.lcdui.Graphics;

import lejos.nxt.Button;
import lejos.util.Delay;

import org.programus.nxj.rockboy.core.World;
import org.programus.nxj.rockboy.core.mc.McUtil;
import org.programus.nxj.rockboy.games.bball.objects.BouncyBall;

public class BBall {
	public static void main(String[] args) {
//		RConsole.openUSB(15000); 

		World.initialize(); 
		
		McUtil util = McUtil.getInstance(); 
//		IOModule input = IOModule.getIOModule(); 
		Graphics g = new Graphics(); 
		g.autoRefresh(false); 

		int x0 = g.getWidth() >> 1; 
		int y0 = ((g.getHeight() + g.getFont().getHeight()) >> 1); 
		
		List<Rectangle> obstacleList = new ArrayList<Rectangle>(); 
		obstacleList.add(new Rectangle(20, 20, 50, 10)); 
		obstacleList.add(new Rectangle(70, 20, 10, 44)); 
		
		Random rand = new Random(); 
		List<Point2D.Double> beans = new ArrayList<Point2D.Double>(); 
		for (int i = 0; i < 5; i++) {
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
			
			beans.add(p); 
//			RConsole.println(p.toString()); 
		}
		
		BouncyBall ball = new BouncyBall(new Point(x0, y0), obstacleList, beans); 
		
		while(!Button.ESCAPE.isPressed()) {
			util.updateAngle(); 
//			RConsole.println("======"); 
			ball.run(); 
			g.clear(); 
//			g.drawString("angle: " + input.getRotationAngle() + "   ", 0, 0); 
			
			Point ballDrawPoint = ball.getTopLeft(); 
//			RConsole.println(ball.getSpeedRef().toString()); 
//			RConsole.println(ball.getCenterPointRef().getLcdPoint().toString()); 
			g.drawImage(ball.getImage(), ballDrawPoint.x, ballDrawPoint.y, false); 
			
			for (Rectangle r : obstacleList) {
				g.fillRect(r.x < 0 ? 0 : r.x, 
						r.y < 0 ? 0 : r.y, 
						r.width > g.getWidth() ? g.getWidth() : r.width, 
						r.height > g.getHeight() ? g.getHeight() : r.height); 
			}
			if (beans.size() > 0) {
				Point2D.Double beanP = beans.get(beans.size() - 1); 
//				RConsole.println(beanP.toString()); 
				g.fillArc((int)beanP.x - 1, (int)beanP.y - 1, 3, 3, 0, 360); 
//				g.setPixel(Graphics.BLACK, (int)beanP.x, (int)beanP.y); 
			} else {
				g.clear(); 
				g.drawString("WIN", 30, 30); 
				g.refresh(); 
				break; 
			}
			g.refresh(); 
			
			Delay.msDelay(20); 
		}
		Button.ESCAPE.waitForPressAndRelease(); 
//		RConsole.close(); 
	}
}
