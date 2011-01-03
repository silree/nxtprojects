package org.programus.nxj.rockboy.core;

import org.programus.nxj.rockboy.core.io.IOModule;
import org.programus.nxj.rockboy.core.mc.McUtil;

/**
 * This class is the world environment of the NXTRockBoy. 
 * Err... actually, there is only one constant now - G. 
 * @author Programus
 *
 */
public class World {
	public static double G = .1; 
	
	public static void initialize() {
		IOModule.getIOModule().resetPosition(); 
		McUtil.getInstance().updateAngle(); 
	}
}
