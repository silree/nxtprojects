package org.programus.nxj.util;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import lejos.nxt.LCD;

public class DisplayUtil {
	public final static int NONE_FLAG = 0; 
	public final static int X_FLAG = 1;
	public final static int Y_FLAG = 1 << 1; 
	
	private final static Graphics g = new Graphics(); 
	
	public static void drawStringCenter(String string, int row) {
		drawStringCenter(string, row * LCD.CELL_HEIGHT, false); 
	}
	
	public static void drawStringCenter(String string, int y, boolean invert) {
		int x = (LCD.SCREEN_WIDTH - string.length() * LCD.CELL_WIDTH) >> 1; 
		LCD.drawString(string, x, y, invert); 
	}
	
	public static void drawStringCenter(String string, int y, int rop) {
		int x = (LCD.SCREEN_WIDTH - string.length() * LCD.CELL_WIDTH) >> 1; 
		LCD.drawString(string, x, y, rop); 
	}
	
	public static void drawStringRight(String string, int y, int right, boolean invert) {
		int x = (LCD.SCREEN_WIDTH - right - string.length() * LCD.CELL_WIDTH); 
		LCD.drawString(string, x, y, invert); 
	}

	public static void drawImageCross(Image image, int x, int y, int flag, int rop) {
		boolean loopX = (flag & X_FLAG) > 0; 
		boolean loopY = (flag & Y_FLAG) > 0; 
		
		g.drawImage(image, 0, 0, x, y, image.getWidth(), image.getHeight(), rop); 
		
		int x1 = x; 
		int y1 = y; 
		
		if (loopX && x + image.getWidth() > LCD.SCREEN_WIDTH) {
			x1 -= LCD.SCREEN_WIDTH; 
			g.drawImage(image, 0, 0, x1, y, image.getWidth(), image.getHeight(), rop); 
		}
		
		if (loopY && y + image.getHeight() > LCD.SCREEN_HEIGHT) {
			y1 -= LCD.SCREEN_HEIGHT; 
			g.drawImage(image, 0, 0, x, y1, image.getWidth(), image.getHeight(), rop); 
		}
		
		if (loopX && loopY) {
			g.drawImage(image, 0, 0, x1, y1, image.getWidth(), image.getHeight(), rop); 
		}
	}
	
	public static void drawRectCross(int x, int y, int w, int h, int flag) {
		boolean loopX = (flag & X_FLAG) > 0; 
		boolean loopY = (flag & Y_FLAG) > 0; 
		
		g.drawRect(x, y, w, h); 
		
		int x1 = x; 
		int y1 = y; 
		
		if (loopX && x + w > LCD.SCREEN_WIDTH) {
			x1 -= LCD.SCREEN_WIDTH; 
			g.drawRect(x1, y, w, h); 
		}
		
		if (loopY && y + h > LCD.SCREEN_HEIGHT) {
			y1 -= LCD.SCREEN_HEIGHT; 
			g.drawRect(x, y1, w, h); 
		}
		
		if (loopX && loopY) {
			g.drawRect(x1, y1, w, h); 
		}
	}
	
	public static void fillRectCross(int x, int y, int w, int h, int flag) {
		boolean loopX = (flag & X_FLAG) > 0; 
		boolean loopY = (flag & Y_FLAG) > 0; 
		
		g.fillRect(x, y, w, h); 
		
		int x1 = x; 
		int y1 = y; 
		
		if (loopX && x + w > LCD.SCREEN_WIDTH) {
			x1 -= LCD.SCREEN_WIDTH; 
			g.fillRect(x1, y, w, h); 
		}
		
		if (loopY && y + h > LCD.SCREEN_HEIGHT) {
			y1 -= LCD.SCREEN_HEIGHT; 
			g.fillRect(x, y1, w, h); 
		}
		
		if (loopX && loopY) {
			g.fillRect(x1, y1, w, h); 
		}
	}
}
