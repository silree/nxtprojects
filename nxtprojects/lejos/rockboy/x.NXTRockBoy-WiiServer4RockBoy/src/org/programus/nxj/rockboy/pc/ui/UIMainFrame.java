package org.programus.nxj.rockboy.pc.ui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class UIMainFrame extends JFrame {
	private static final long serialVersionUID = -4488560941915727698L;
	
	private void initComponents() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); 
		
		PaintPanel p = new PaintPanel(); 
		Container contentPane = this.getContentPane(); 
		contentPane.setLayout(new BorderLayout()); 
		contentPane.add(p, BorderLayout.CENTER); 
	}
	
	public UIMainFrame() {
		this.initComponents(); 
	}
}
