package org.programus.nxj.util;

/**
 * Just to override bugs in java.lang.Math. 
 * @author Programus
 *
 */
public class MathUtil {
	public static long round(double x) {
		long l = (long) x; 
		double d = x - l; 
		double ad = Math.abs(d); 
		if (ad < .5) {
			return l; 
		} else {
			return x > 0 ? l + 1 : l - 1; 
		}
	}
	
	public static int round(float x) {
		int l = (int) x; 
		float d = x - l; 
		float ad = Math.abs(d); 
		if (ad < .5) {
			return l; 
		} else {
			return x > 0 ? l + 1 : l - 1; 
		}
	}
}
