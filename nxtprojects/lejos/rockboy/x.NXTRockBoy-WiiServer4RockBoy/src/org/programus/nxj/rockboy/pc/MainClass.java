package org.programus.nxj.rockboy.pc;

import javax.swing.SwingUtilities;

import org.programus.nxj.rockboy.pc.comm.GlobalObjects;
import org.programus.nxj.rockboy.pc.comm.ShutdownHook;
import org.programus.nxj.rockboy.pc.ui.UIData;
import org.programus.nxj.rockboy.pc.ui.UIMainFrame;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final UIMainFrame frame = new UIMainFrame(); 
		UIData.getInstance().setFrame(frame); 
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				frame.pack(); 
				frame.setLocationRelativeTo(null); 
				frame.setVisible(true); 
			}
		}); 
		GlobalObjects.getInstance(); 
		
		Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook(), "Shutdown-Hook")); 
	}
}
