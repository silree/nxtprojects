package org.programus.nxj.rockboy.pc.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.text.MessageFormat;

import javax.swing.JPanel;

import org.programus.nxj.rockboy.pc.wii.WiimoteData;

public class PaintPanel extends JPanel {
	private static final long serialVersionUID = -18701287326127380L;
	
	private static final MessageFormat mf = new MessageFormat("angle: {0,number,000.00000} / {1,number,000.00000}"); 

	public PaintPanel() {
		this.setPreferredSize(new Dimension(400, 400)); 
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Rectangle r = g.getClipBounds(); 
		Graphics2D g2d = (Graphics2D) g; 
		
		double angle = WiimoteData.getInstance().getAngle(); 
		double angle4NXT = WiimoteData.getInstance().getAngle4NXT(); 
		double l = Math.min(r.height, r.width) / 2 - 3; 
		double dx = l * Math.cos(Math.toRadians(angle - 90)); 
		double dy = l * Math.sin(Math.toRadians(angle - 90)); 
		Point2D.Double o = new Point2D.Double(r.getCenterX(), r.getCenterY()); 
		Point2D.Double p = new Point2D.Double(o.x + dx, o.y + dy); 
		
		Line2D.Double l2d = new Line2D.Double(o, p); 
		g2d.setColor(Color.BLACK); 
		g2d.draw(l2d); 
		
		String msg = mf.format(new Double[]{angle4NXT, angle}); 
		g2d.drawString(msg, 0, r.height); 
		
		dx = l * Math.cos(Math.toRadians(angle4NXT - angle - 90)); 
		dy = l * Math.sin(Math.toRadians(angle4NXT - angle - 90)); 
		Ellipse2D.Double e = new Ellipse2D.Double(); 
		e.width = 3; 
		e.height = 3;
		e.x = r.getCenterX() + dx - 1.5;
		e.y = r.getCenterY() + dy - 1.5; 
		
		g2d.setColor(Color.RED); 
		g2d.fill(e); 
	}
}
