package org.programus.nxj.rockboy.games.bball;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.microedition.lcdui.Graphics;

import lejos.nxt.Button;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

import org.programus.nxj.rockboy.core.World;
import org.programus.nxj.rockboy.core.input.InputModule;
import org.programus.nxj.rockboy.core.mc.McPoint;
import org.programus.nxj.rockboy.core.mc.McUtil;
import org.programus.nxj.rockboy.core.mc.McVector;
import org.programus.nxj.rockboy.games.bball.objects.BouncyBall;

public class BBall {
	public static void main(String[] args) {
//		RConsole.openUSB(15000); 

		World.initialize(); 
		
		McUtil util = McUtil.getInstance(); 
		InputModule input = InputModule.getInputModule(); 
		Graphics g = new Graphics(); 
		g.autoRefresh(false); 
		
		int length = 30; 
		McVector vector = new McVector(); 
		vector.setStaticInLcd(false); 
		vector.setNaturalVector(length, -90); 
		int x0 = g.getWidth() >> 1; 
		int y0 = ((g.getHeight() + g.getFont().getHeight()) >> 1); 
		McPoint p = new McPoint(); 
		p.setStaticInLcd(true); 
		p.setLcdPoint(x0, y0); 
		McPoint xLineP1 = new McPoint(); 
		xLineP1.setStaticInLcd(false); 
		xLineP1.setNaturalPoint(-100, 0); 
		McPoint xLineP2 = new McPoint(xLineP1); 
		xLineP2.setNaturalPoint(100, 0); 
		McPoint yLineP1 = new McPoint(xLineP1); 
		yLineP1.setNaturalPoint(0, -100); 
		McPoint yLineP2 = new McPoint(yLineP1); 
		yLineP2.setNaturalPoint(0, 100); 
		
		BouncyBall ball = new BouncyBall(new Point(x0, y0), new ArrayList<Rectangle2D.Double>()); 

		while(!Button.ESCAPE.isPressed()) {
			util.updateAngle(); 
			RConsole.println("======"); 
			ball.run(); 
			g.clear(); 
			g.drawString("angle: " + input.getRotationAngle() + "   ", 0, 0); 
			McPoint p2 = p.moveNew(vector); 
			Point2D.Double lcdP = p2.getLcdPoint(); 
			g.drawLine(x0, y0, (int) Math.round(lcdP.x), (int) Math.round(lcdP.y)); 
			Point2D.Double lcdP1 = xLineP1.getLcdPoint(); 
			Point2D.Double lcdP2 = xLineP2.getLcdPoint(); 
			g.drawLine((int)Math.round(lcdP1.x), (int)Math.round(lcdP1.y), 
					(int)Math.round(lcdP2.x), (int)Math.round(lcdP2.y)); 
			lcdP1 = yLineP1.getLcdPoint(); 
			lcdP2 = yLineP2.getLcdPoint(); 
			g.drawLine((int)Math.round(lcdP1.x), (int)Math.round(lcdP1.y), 
					(int)Math.round(lcdP2.x), (int)Math.round(lcdP2.y)); 
			
			Point ballDrawPoint = ball.getTopLeft(); 
//			RConsole.println(ball.getSpeedRef().toString()); 
//			RConsole.println(ball.getCenterPointRef().getLcdPoint().toString()); 
			g.drawImage(ball.getImage(), ballDrawPoint.x, ballDrawPoint.y, false); 
			
			g.refresh(); 
			
			Delay.msDelay(20); 
		}
		Button.ESCAPE.waitForPressAndRelease(); 
//		RConsole.close(); 
	}
}
