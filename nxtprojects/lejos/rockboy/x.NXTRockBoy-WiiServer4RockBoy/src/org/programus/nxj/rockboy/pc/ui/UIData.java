package org.programus.nxj.rockboy.pc.ui;

import javax.swing.JFrame;

public class UIData {
	private JFrame frame;

	/**
	 * @return the frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame the frame to set
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	private static UIData ud = new UIData();
	private UIData() {}
	
	public static UIData getInstance() {
		return ud; 
	}
}
