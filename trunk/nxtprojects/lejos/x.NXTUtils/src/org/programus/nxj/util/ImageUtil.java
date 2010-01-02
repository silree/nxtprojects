package org.programus.nxj.util;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.microedition.lcdui.Image;

/**
 * Image utility. A collection of some image related functions. 
 * 
 * @author Programus
 *
 */
public class ImageUtil {
	/**
	 * Read image from file. An image file has below format:
	 * <table border="1">
	 * <tr>
	 * <th>1st byte - 4th byte</th>
	 * <th>5th byte - 8th byte</th>
	 * <th>9th byte</th>
	 * <th>10th byte ....</th>
	 * </tr>
	 * <tr>
	 * <td><i>image-width (int)</i></td>
	 * <td><i>image-height (int)</i></td>
	 * <td><code>0x00</code>(<i>image data delimit</i>)</td>
	 * <td><i>byte image data</i>....</td>
	 * </table>
	 * <p>
	 * For example:
	 * </p>
	 * After a file with content 
	 * <table border="1">
	 * <tr>
	 * <th>width (int)</th>
	 * <th>height (int)</th>
	 * <th>delimit</th>
	 * <th colspan="3">byte data</th>
	 * </tr>
	 * <tr>
	 * <td>00 00 00 03</td>
	 * <td>00 00 00 05</td>
	 * <td>00</td>
	 * <td>00</td>
	 * <td>02</td>
	 * <td>1f</td>
	 * </tr>
	 * </table>
	 * was read, this method will return an object which is equivalent to
	 * <div style="margin-left:4em;"><code>new Image(3, 5, new byte[] {(byte)0x00, (byte)0x02, (byte)0x1f})</code></div>
	 * @param file image file
	 * @return an nxt image object.
	 * @throws IOException if an input or output error occurs or file format is not correct. 
	 * @see Image
	 * @see Image#Image(int, int, byte[])
	 */
	public static Image readImage(File file) throws IOException {
		DataInputStream in = new DataInputStream(new FileInputStream(file)); 
		int w;
		int h;
//		byte[] bytes = new byte[(int)(file.length() - 3)]; 
		byte[] bytes = new byte[(int)(file.length() - (4 + 4 + 1))]; // two int(4*byte) + one 0x00
		int index = 0; 
		try {
			// byte size -> int size. 
//			w = in.read(); 
//			if (w < 0) {
//				throw new IOException("File format error!"); 
//			}
//			h = in.read(); 
//			if (h < 0) {
//				throw new IOException("File format error!"); 
//			}
			try {
				w = in.readInt(); 
				h = in.readInt();
			} catch (EOFException e) {
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
