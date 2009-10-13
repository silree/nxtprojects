package org.programus.nxj.rockboy.core;

import org.programus.nxj.rockboy.core.io.IOModule;
import org.programus.nxj.rockboy.core.mc.McUtil;

public class World {
	public static double G = .1; 
	
	public static void initialize() {
		IOModule.getIOModule().resetPosition(); 
		McUtil.getInstance().updateAngle(); 
	}
}
