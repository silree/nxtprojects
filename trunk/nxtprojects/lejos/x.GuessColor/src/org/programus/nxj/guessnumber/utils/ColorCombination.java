package org.programus.nxj.guessnumber.utils;

import java.util.Random;

import lejos.robotics.Colors;

public class ColorCombination {
	public static final int COLOR_NUM = 4;
	
	public static final int[] COLOR_TYPES = {Colors.RED, Colors.YELLOW, Colors.GREEN, Colors.BLUE}; 
	public static final String[] COLOR_TYPE_DESCS = {"R", "Y", "G", "B", "X"}; 

	private int[] colorIndices; 
	public ColorCombination(int[] colors) {
		colorIndices = new int[colors.length]; 
		for (int i = 0; i < colors.length; i++) {
			colorIndices[i] = -1;
			for (int j = 0; j < COLOR_TYPES.length; j++) {
				if (colors[i] == COLOR_TYPES[j]) {
					colorIndices[i] = j;
				}
			}
			if (colorIndices[i] < 0) {
				colorIndices[i] = COLOR_TYPES.length;
			}
		}
	}
	
	public static ColorCombination getRandomCombination(int countLimit, boolean allowBlank) {
		return getRandomCombination(allowBlank ? COLOR_TYPES.length + 1 : COLOR_TYPES.length, countLimit); 
	}
	
	public static ColorCombination getRandomCombination(int colorTypeNumber, int countLimit) {
		int[] allAvailableColors = new int[colorTypeNumber * countLimit]; 
		for (int i = 0; i < countLimit; i++) {
			System.arraycopy(COLOR_TYPES, 0, allAvailableColors, i * colorTypeNumber, COLOR_TYPES.length); 
		}
		Random r = new Random(); 
		for (int i = allAvailableColors.length; i > allAvailableColors.length - COLOR_NUM; i--) {
			int ii = i - 1;
			int jj = r.nextInt(i); 
			int tmp = allAvailableColors[ii]; 
			allAvailableColors[ii] = allAvailableColors[jj]; 
			allAvailableColors[jj] = tmp;
		}
		int[] colors = new int[COLOR_NUM]; 
		System.arraycopy(allAvailableColors, allAvailableColors.length - COLOR_NUM - 1, colors, 0, COLOR_NUM); 
		return new ColorCombination(colors); 
	}
	
	public CompareResult compareTo(ColorCombination cc) {
		CompareResult r = new CompareResult(); 
		for (int i = 0; i < this.colorIndices.length; i++) {
			boolean exact = false;
			boolean exist = false;
			for (int j = 0; j < cc.colorIndices.length; j++) {
				if (this.colorIndices[i] == cc.colorIndices[j]) {
					if (i == j) {
						exact = true; 
					} else {
						exist = true;
					}
				}
			}
			if (exact) {
				r.exactCorrect++; 
			} else if (exist) {
				r.existCorrect++;
			}
		}
		return r; 
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(); 
		sb.append('['); 
		for (int i = 0; i < this.colorIndices.length; i++) {
			sb.append(this.colorIndices[i]); 
		}
		sb.append(']'); 
		return sb.toString(); 
	}
	
	public String toLcdString() {
		StringBuffer sb = new StringBuffer(); 
		sb.append('['); 
		for (int i = 0; i < this.colorIndices.length; i++) {
			sb.append(COLOR_TYPE_DESCS[this.colorIndices[i]]); 
		}
		sb.append(']'); 
		return sb.toString(); 
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ColorCombination)) {
			return false; 
		}
		ColorCombination cc = (ColorCombination) o; 
		return cc == null ? false : cc.hashCode() == this.hashCode();
	}
	
	@Override
	public int hashCode() {
		int ret = 0; 
		for (int i = 0; i < this.colorIndices.length; i++) {
			ret = (ret << 1) + this.colorIndices[i]; 
		}
		return ret; 
	}
}
