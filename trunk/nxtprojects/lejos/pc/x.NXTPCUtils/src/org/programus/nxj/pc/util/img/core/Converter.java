package org.programus.nxj.pc.util.img.core;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class Converter {
	
	public static BufferedImage removeColor(BufferedImage colorImage, int threshold) {
		if (colorImage.getType() == BufferedImage.TYPE_BYTE_BINARY) {
			return colorImage; 
		}
		int w = colorImage.getWidth(); 
		int h = colorImage.getHeight(); 
		int[] argbs = colorImage.getRGB(0, 0, w, h, null, 0, w); 
		int[] bws = new int[argbs.length]; 
		for (int i = 0; i < argbs.length; i++) {
			bws[i] = getH(argbs[i]) > threshold ? 0xffffffff : 0; 
		}
		BufferedImage image = new BufferedImage(colorImage.getWidth(), colorImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY); 
		image.setRGB(0, 0, w, h, bws, 0, w); 
		return image; 
	}
	
	public static byte[] nxtImageConvert(BufferedImage image) {
		if (image == null || image.getType() != BufferedImage.TYPE_BYTE_BINARY) {
			return null; 
		}
		int w = image.getWidth(); 
		int h = image.getHeight(); 
		int n = h >> 3; 
		if (h > n << 3) {
			n++; 
		}
		
		byte[] data = new byte[n * w]; 
		int index = 0; 
		for (int i = 0; i < h; i += 8) {
			for (int j = 0; j < w; j++) {
				byte d = 0; 
				for (int k = 7; k >= 0; k--) {
					d <<= 1;
					int x = j; 
					int y = i + k; 
					if (y < h) {
						int argb = image.getRGB(x, y); 
						d |= (byte) ((argb & 0xffffff) > 0 ? 0 : 1); 
					}
				}
				data[index++] = d; 
			}
		}
				
		return data; 
	}
	
	public static String getImageCreateString(byte[] data, Dimension size) {
		StringBuilder sb = new StringBuilder("new Image("); 
		// width and height
		sb.append(size.width).append(", ").append(size.height).append(", "); 
		// new byte[]
		sb.append("new byte[] {"); 
		
		for (byte b : data) {
			sb.append("(byte) 0x"); 
			String hex = Integer.toHexString(0xff & (int)b); 
			if (hex.length() < 2) {
				sb.append('0'); 
			}
			sb.append(hex); 
			sb.append(", "); 
		}
		
		sb.append("});"); 
		
		return sb.toString(); 
	}
	
	private static int getH(int argb) {
		int b = (argb & 0xff); 
		argb >>>= 8; 
		int g = (argb & 0xff); 
		argb >>>= 8; 
		int r = (argb & 0xff); 
		int max = b > g ? b : g; 
		return max > r ? max : r; 		
	}
}
