package org.programus.nxj.util;

import lejos.nxt.LCD;

public class DisplayUtil {
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
}
