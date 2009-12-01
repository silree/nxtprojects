package org.programus.nxj.util.txtimg;


public class TextImage3x5 extends TextImage {
	
	private final static byte[][] numberData = {
		{0x1f, 0x11, 0x1f}, // 0
		{0x00, 0x1f, 0x00}, // 1
		{0x1d, 0x15, 0x17}, // 2
		{0x15, 0x15, 0x1f}, // 3
		{0x07, 0x04, 0x1f}, // 4
		{0x17, 0x15, 0x1d}, // 5
		{0x1f, 0x15, 0x1d}, // 6
		{0x01, 0x01, 0x1f}, // 7
		{0x1f, 0x15, 0x1f}, // 8
		{0x17, 0x15, 0x1f}, // 9
	}; 
	
	private final static byte[][] otherData = {
		{0x04, 0x04, 0x04}, // -
		{0x00, 0x00, 0x00}, // blank
	}; 

	@Override
	protected int getCharHeight() {
		return 5;
	}

	@Override
	protected int getCharWidth() {
		return 3;
	}

	@Override
	protected byte[] getImageData(int i) {
		return numberData[i];
	}

	@Override
	protected byte[] getImageData(char c) {
		if (c == '-') {
			return otherData[0]; 
		} else {
			return otherData[1]; 
		}
	}
}
