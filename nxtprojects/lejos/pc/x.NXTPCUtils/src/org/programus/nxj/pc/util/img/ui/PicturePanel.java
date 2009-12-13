package org.programus.nxj.pc.util.img.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.programus.nxj.pc.util.img.core.Converter;

public class PicturePanel extends JPanel {
	/** SN */
	private static final long serialVersionUID = -5041297685815342536L;
	
	public static final String IMAGE_UPDATE_PROP = "imageUpdated!"; 
	
	private JLabel imageLabel = new JLabel(); 
	private JSlider thresholdSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 0xff, 0xff >> 1); 
	
	private BufferedImage originImage; 
	private BufferedImage blackwhiteImage; 
	
	private int counter = 0; 
	
	public PicturePanel() {
		super(); 
		this.allocateComponents(); 
	}
	
	protected void allocateComponents() {
		this.setLayout(new BorderLayout()); 
		this.add(new JScrollPane(this.imageLabel), BorderLayout.CENTER); 
		this.add(this.thresholdSlider, BorderLayout.SOUTH); 

		this.imageLabel.setHorizontalAlignment(SwingConstants.CENTER); 
		this.thresholdSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				PicturePanel.this.setThreshold(PicturePanel.this.thresholdSlider.getValue()); 
			}
		}); 
	}
	
	public void setImage(BufferedImage image) {
		this.thresholdSlider.setEnabled(image.getType() != BufferedImage.TYPE_BYTE_BINARY); 
		this.originImage = image; 
		this.convertImage(); 
		this.updateImage(); 
	}
	
	public void setThreshold(int value) {
		this.thresholdSlider.setValue(value); 
		this.convertImage(); 
		this.updateImage(); 
	}
	
	protected void convertImage() {
		if (this.originImage != null) {
			this.blackwhiteImage = Converter.removeColor(this.originImage, this.thresholdSlider.getValue()); 
		}
	}
	
	protected void updateImage() {
		if (this.blackwhiteImage != null) {
			this.imageLabel.setIcon(new ImageIcon(this.blackwhiteImage)); 
			this.imageLabel.validate(); 
			this.firePropertyChange(IMAGE_UPDATE_PROP, counter++, counter); 
		}
	}
	
	public byte[] getNxtImageData() {
		return Converter.nxtImageConvert(this.blackwhiteImage); 
	}
	
	public Dimension getImageSize() {
		return new Dimension(this.blackwhiteImage.getWidth(), this.blackwhiteImage.getHeight()); 
	}
}
