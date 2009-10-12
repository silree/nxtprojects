package org.programus.nxj.rockboy.core;

import org.programus.nxj.rockboy.core.input.InputModule;
import org.programus.nxj.rockboy.core.mc.McUtil;

public class World {
	public static double G = .6; 
	
	public static void initialize() {
		InputModule.getInputModule().resetPosition(); 
		McUtil.getInstance().updateAngle(); 
	}
}
