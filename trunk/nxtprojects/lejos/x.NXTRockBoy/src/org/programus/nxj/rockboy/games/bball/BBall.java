package org.programus.nxj.rockboy.games.bball;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

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
		obstacleList.add(new Rectangle(-50, 30, 200, 5)); 
//		obstacleList.add(new Rectangle(80, 20, 5, 44)); 
		
		BouncyBall ball = new BouncyBall(new Point(x0, y0), obstacleList); 
		
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
			
			g.refresh(); 
			
			Delay.msDelay(20); 
		}
		Button.ESCAPE.waitForPressAndRelease(); 
//		RConsole.close(); 
	}
}
