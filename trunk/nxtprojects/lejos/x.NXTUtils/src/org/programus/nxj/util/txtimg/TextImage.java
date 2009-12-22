package org.programus.nxj.util.txtimg;

import javax.microedition.lcdui.Image;

/**
 * An abstract base class for all Text Image classes. Please extend this class to implement your own TextImage classes. 
 * <p>Normally, we use this like below. 
 * <code><pre>
 * TextImage ti = new TextImage3x5(); 
 * Image message = ti.getImage("MESSAGE"); 
 * DisplayUtil.drawImageCenter(message, (LCD.SCREEN_HEIGHT >> 1), LCD.ROP_XOR); 
 * </pre></code>
 * </p>
 * @author Programus
 *
 */
public abstract class TextImage {
	
	/**
	 * Return an image for the specified text string. 
	 * @param text
	 * @return an image for the specified text string. 
	 */
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
	
	/**
	 * Return the image for the specified character. 
	 * @param c the specified character. 
	 * @return the image for the specified character. 
	 */
	public Image getImage(char c) {
		return new Image(this.getCharWidth(), this.getCharHeight(), this.getImageData(c)); 
	}
	
	/**
	 * Return the image for the specified number. 
	 * This is normally faster than getImage(String.valueOf(n)) and using less memory. 
	 * @param n
	 * @return the image for the specified number. 
	 */
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
	
	/**
	 * Return the image data for the specified number. 
	 * This method should use only the last digital of the specified number if it is greater than 10. 
	 * @param i
	 * @return the image data for the specified number. 
	 */
	protected abstract byte[] getImageData(int i); 	
	/**
	 * Return the image data for the specified character. 
	 * @param c
	 * @return the image data for the specified character. 
	 */
	protected abstract byte[] getImageData(char c); 
	
	/**
	 * Return the width of the character. 
	 * @return the height of the character. 
	 */
	protected abstract int getCharWidth(); 
	/**
	 * Return the height of the character. 
	 * @return the height of the character. 
	 */
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
