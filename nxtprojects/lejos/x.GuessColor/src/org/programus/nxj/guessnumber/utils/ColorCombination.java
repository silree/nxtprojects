package org.programus.nxj.guessnumber.utils;

import java.util.Random;

public class ColorCombination {
	public static final int COLOR_NUM = 4;
	
	private int[] colors; 
	public ColorCombination(int[] colors) {
		this.colors = colors;
	}
	
	public static ColorCombination getRandomCombination(int[] colorTypes, int countLimit) {
		int[] allColors = new int[colorTypes.length * countLimit]; 
		for (int i = 0; i < countLimit; i++) {
			System.arraycopy(colorTypes, 0, allColors, i * colorTypes.length, colorTypes.length); 
		}
		return getRandomCombination(allColors); 
	}
	
	public static ColorCombination getRandomCombination(int[] allAvailableColors) {
		Random r = new Random(); 
		for (int i = allAvailableColors.length; i > 1; i--) {
			int ii = i - 1;
			int jj = r.nextInt(i); 
			int tmp = allAvailableColors[ii]; 
			allAvailableColors[ii] = allAvailableColors[jj]; 
			allAvailableColors[jj] = tmp;
		}
		int[] colors = new int[COLOR_NUM]; 
		System.arraycopy(allAvailableColors, 0, colors, 0, COLOR_NUM); 
		return new ColorCombination(colors); 
	}
	
	public CompareResult compareTo(ColorCombination cc) {
		CompareResult r = new CompareResult(); 
		for (int i = 0; i < this.colors.length; i++) {
			boolean exact = false;
			boolean exist = false;
			for (int j = 0; j < cc.colors.length; j++) {
				if (this.colors[i] == cc.colors[j]) {
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
		for (int i = 0; i < this.colors.length; i++) {
			sb.append(this.colors[i]); 
		}
		sb.append(']'); 
		return sb.toString(); 
	}
	
	public String toLcdString() {
		StringBuffer sb = new StringBuffer(); 
		sb.append('['); 
		for (int i = 0; i < this.colors.length; i++) {
			int index = 0;
			for (int j = 0; j < Constants.COLOR_TYPES.length; j++) {
				if (Constants.COLOR_TYPES[j] == this.colors[i]) {
					index = j; 
				}
			}
			sb.append(Constants.COLOR_TYPE_DESCS[index]); 
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
		for (int i = 0; i < this.colors.length; i++) {
			ret = (ret << 1) + this.colors[i]; 
		}
		return ret; 
	}
}
