package org.programus.nxj.util.txtimg;

import javax.microedition.lcdui.Image;

public abstract class TextImage {
	
	public Image getImage(String text) {
		int size = text.length(); 
        int step = this.getCharWidth() + 1; 
        byte[] data = new byte[size * step]; 
        byte[] d = null; 
        char[] chars = text.toCharArray(); 
        int i = 0; 
        for (char c : chars) {
    		d = this.getImageData(c); 
    		System.arraycopy(d, 0, data, i, this.getCharWidth()); 
    		i += step; 
        }
        return new Image(step * size - 1, this.getCharHeight(), data); 
	}
	
	public Image getImage(char c) {
		return new Image(this.getCharWidth(), this.getCharHeight(), this.getImageData(c)); 
	}
	
	public Image getImage(int n) {
		Image ret = null; 
		if (n >= 0 && n < 10) {
			ret = new Image(this.getCharWidth(), this.getCharHeight(), this.getImageData(n)); 
		} else {
	        int size = (n < 0) ? stringSize(-n) + 1 : stringSize(n); 
	        int step = this.getCharWidth() + 1; 
	        byte[] data = new byte[size * step]; 
	        byte[] d = null; 
	        if (n < 0) {
	        	d = this.getImageData('-'); 
	        	System.arraycopy(d, 0, data, 0, this.getCharWidth()); 
	        	n = -n; 
	        }
	        for (int i = data.length - step; i >= 0 && n > 0; i -= step) {
	        	int x = n % 10; 
	    		d = this.getImageData(x); 
	    		System.arraycopy(d, 0, data, i, this.getCharWidth()); 
	        	n /= 10; 
	        }
	        ret = new Image(step * size - 1, this.getCharHeight(), data); 
		}
		
		return ret; 
	}
	
	protected abstract byte[] getImageData(int i); 	
	protected abstract byte[] getImageData(char c); 
	
	protected abstract int getCharWidth(); 
	protected abstract int getCharHeight(); 

    final static int [] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999,
                                      99999999, 999999999, Integer.MAX_VALUE };

    // Requires positive x
    static int stringSize(int x) {
        for (int i=0; ; i++)
            if (x <= sizeTable[i])
                return i+1;
    }
}
