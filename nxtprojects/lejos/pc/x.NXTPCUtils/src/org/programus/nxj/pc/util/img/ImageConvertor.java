package org.programus.nxj.pc.util.img;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.programus.nxj.pc.util.img.ui.MainPanel;


public class ImageConvertor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final JFrame frame = new JFrame("Image Convertor"); 
		MainPanel panel = new MainPanel(); 
		frame.getContentPane().add(panel); 
		frame.setJMenuBar(panel.getMenuBar(panel)); 
		frame.setLocationRelativeTo(null); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.pack(); 
		frame.setSize(500, 300); 
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true); 
			}
		}); 
	}
	
}
