package org.programus.nxj.util;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import lejos.nxt.LCD;

/**
 * Display utility. A collection of advanced LCD screen display functions
 * @author Programus
 * 
 * @see LCD
 * @see Graphics
 */
public class DisplayUtil {
	
	/**
	 * Used by drawXxxCross functions, NONE_FLAG means no cross needed, in other words, just like normal draw function. 
	 */
	public final static int NONE_FLAG = 0; 
	/**
	 * Used by drawXxxCross functions, X_FLAG means it will be drawn cross left or right boundary. 
	 */
	public final static int X_FLAG = 1;
	/**
	 * Used by drawXxxCross functions, Y_FLAG means it will be drawn cross top or bottom boundary. 
	 */
	public final static int Y_FLAG = 1 << 1; 
	
	private final static Graphics g = new Graphics(); 
	
	/**
	 * Draw a string on the LCD screen center alignment.
	 * @param string the string to be drawn
	 * @param row the row number to draw the string. The row num should be 0-7. 
	 */
	public static void drawStringCenter(String string, int row) {
		drawStringCenter(string, row * LCD.CELL_HEIGHT, false); 
	}
	
	/**
	 * Draw a string on the LCD screen center alignment.
	 * @param string the string to be drawn
	 * @param y the vertical position to draw the string
	 * @param invert true if you want to draw the string invert in color. 
	 * @see LCD#drawString(String, int, int, boolean)
	 */
	public static void drawStringCenter(String string, int y, boolean invert) {
		int x = (LCD.SCREEN_WIDTH - string.length() * LCD.CELL_WIDTH) >> 1; 
		LCD.drawString(string, x, y, invert); 
	}
	
	/**
	 * Draw a string on the LCD screen center alignment.
	 * @param string the string to be drawn
	 * @param y the vertical position to draw the string
	 * @param rop draw rop 
	 * @see LCD#drawString(String, int, int, int)
	 */
	public static void drawStringCenter(String string, int y, int rop) {
		int x = (LCD.SCREEN_WIDTH - string.length() * LCD.CELL_WIDTH) >> 1; 
		LCD.drawString(string, x, y, rop); 
	}
	
	/**
	 * Draw a string on the LCD screen right alignment. 
	 * @param string the string to be drawn
	 * @param y the vertical position to draw the string
	 * @param right right margin
	 * @param invert true if you want to draw the string invert in color. 
	 * @see LCD#drawString(String, int, int, boolean)
	 */
	public static void drawStringRight(String string, int y, int right, boolean invert) {
		int x = (LCD.SCREEN_WIDTH - right - string.length() * LCD.CELL_WIDTH); 
		LCD.drawString(string, x, y, invert); 
	}
	
	/**
	 * Draw a string on the LCD screen right alignment. 
	 * @param image
	 * @param y
	 * @param rop
	 */
	public static void drawImageCenter(Image image, int y, int rop) {
		int x = (LCD.SCREEN_WIDTH - image.getWidth()) >> 1; 
		g.drawImage(image, 0, 0, x, y, image.getWidth(), image.getHeight(), rop); 
	}
	
	/**
	 * Draw an image on the LCD screen right alignment. 
	 * @param image the image to be drawn
	 * @param y the vertical position to draw the string
	 * @param right right margin
	 * @param rop draw rop 
	 * @see Graphics#drawImage(Image, int, int, int, int, int, int, int)
	 */
	public static void drawImageRight(Image image, int y, int right, int rop) {
		int x = (LCD.SCREEN_WIDTH - right - image.getWidth()); 
		g.drawImage(image, 0, 0, x, y, image.getWidth(), image.getHeight(), rop); 
	}

	/**
	 * Draw an image cross the LCD screen. 
	 * @param image the image to be drawn
	 * @param x
	 * @param y
	 * @param flag
	 * @param rop
	 * @see DisplayUtil#drawRectCross(int, int, int, int, int)
	 */
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
	
	/**
	 * Draw a rectangle cross the LCD screen. 
	 * <p>Example:</p>
	 * <div style="font-family:monospace;">
	 * <pre>
	 * Rect: 
	 *  +-------+ 
	 *  +-------+
	 * When you draw this Rect by applying X_FLAG:
	 * +-----------------------------+
	 * |                             |
	 * |                      (x,y)  |
	 * |----+                    +---|
	 * |----+                    +---|
	 * |                             | <- Screen boundary
	 * |                             |
	 * |                             |
	 * +-----------------------------+
	 * </pre>
	 * </div>
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param flag
	 */
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
	
	/**
	 * Draw a rectangle cross the LCD screen. 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param flag
	 * @see DisplayUtil#drawRectCross(int, int, int, int, int)
	 */
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
