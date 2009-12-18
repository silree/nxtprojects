package org.programus.nxj.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.microedition.lcdui.Image;

public class ImageUtil {
	public static Image readImage(File file) throws IOException {
		FileInputStream in = new FileInputStream(file); 
		int w;
		int h;
		byte[] bytes = new byte[(int)(file.length() - 3)]; 
		int index = 0; 
		try {
			w = in.read(); 
			if (w < 0) {
				throw new IOException("File format error!"); 
			}
			h = in.read(); 
			if (h < 0) {
				throw new IOException("File format error!"); 
			}
			int i = in.read(); 
			if (i != 0) {
				throw new IOException("File format error!"); 
			}
			do {
				i = in.read(); 
				if (i >= 0) {
					bytes[index++] = (byte) i; 
				} else {
					break; 
				}
			} while (i >= 0);
		} catch (IOException e) {
			throw e; 
		} finally {
			in.close(); 
		}
		return new Image(w, h, bytes); 
	}
}
