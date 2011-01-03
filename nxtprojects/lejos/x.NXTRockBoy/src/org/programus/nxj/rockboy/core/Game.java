package org.programus.nxj.rockboy.core;

import org.programus.nxj.rockboy.core.io.IOModule;

public class Game {
	public void quit() {
		IOModule.getIOModule().stop(); 
		System.exit(0); 
	}
}
