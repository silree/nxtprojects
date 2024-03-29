package org.programus.nxj.pc.util.img.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.programus.nxj.pc.util.img.core.Converter;

public class PicturePanel extends JPanel {
	/** SN */
	private static final long serialVersionUID = -5041297685815342536L;
	
	public static final String IMAGE_UPDATE_PROP = "imageUpdated!"; 
	
	private JLabel imageLabel = new JLabel(); 
	
	private JLabel thresholdLabel = new JLabel("Threshold: "); 
	private JSlider thresholdSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 0xff, 0xff >> 1); 
	private SpinnerNumberModel thresholdSpinnerModel = new SpinnerNumberModel(0xff >> 1, 0, 0xff, 1); 
	private JSpinner thresholdSpinner = new JSpinner(this.thresholdSpinnerModel); 
	
	private BufferedImage originImage; 
	private BufferedImage blackwhiteImage; 
	
	private TimerTask thresholdTask; 
	
	public PicturePanel() {
		super(); 
		this.allocateComponents(); 
	}
	
	protected void allocateComponents() {
		this.setLayout(new BorderLayout()); 
		this.add(new JScrollPane(this.imageLabel), BorderLayout.CENTER); 
		JPanel panel = new JPanel(); 
		panel.setLayout(new BorderLayout()); 
		panel.add(this.thresholdSlider, BorderLayout.CENTER); 
		panel.add(this.thresholdSpinner, BorderLayout.EAST); 
		panel.add(this.thresholdLabel, BorderLayout.WEST); 
		this.add(panel, BorderLayout.SOUTH); 
		
		this.enableThresholdControls(false); 

		this.imageLabel.setHorizontalAlignment(SwingConstants.CENTER); 
		
		final Timer timer = new Timer("Threshold Timer", true); 
		this.thresholdSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				final int v = PicturePanel.this.thresholdSlider.getValue(); 
				PicturePanel.this.thresholdSpinnerModel.setValue(v); 
			}
		}); 
		this.thresholdSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				final int v = PicturePanel.this.thresholdSpinnerModel.getNumber().intValue(); 
				PicturePanel.this.thresholdSlider.setValue(v); 
				
				if (thresholdTask != null) {
					thresholdTask.cancel(); 
					timer.purge(); 
				}
				thresholdTask = new TimerTask() {
					@Override
					public void run() {
						PicturePanel.this.setThreshold(v); 
					}
				}; 
				timer.schedule(thresholdTask, 100); 
			}
		}); 
	}
	
	protected void enableThresholdControls(boolean enabled) {
		this.thresholdLabel.setEnabled(enabled); 
		this.thresholdSlider.setEnabled(enabled); 
		this.thresholdSpinner.setEnabled(enabled); 
	}
	
	public void setImage(BufferedImage image) {
		this.enableThresholdControls(image.getType() != BufferedImage.TYPE_BYTE_BINARY); 
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
			this.firePropertyChange(IMAGE_UPDATE_PROP, false, true); 
		}
	}
	
	public byte[] getNxtImageData() {
		return Converter.nxtImageConvert(this.blackwhiteImage); 
	}
	
	public Dimension getImageSize() {
		return new Dimension(this.blackwhiteImage.getWidth(), this.blackwhiteImage.getHeight()); 
	}
}
