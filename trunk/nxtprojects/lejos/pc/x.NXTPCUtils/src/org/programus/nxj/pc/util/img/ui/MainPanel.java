package org.programus.nxj.pc.util.img.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.programus.nxj.pc.util.img.core.Converter;

public class MainPanel extends JPanel {
	/** SN */
	private static final long serialVersionUID = -2222575532385000674L;
	
	private PicturePanel picPanel = new PicturePanel(); 
	private JTextArea nxtText = new JTextArea(5, 50); 
	
	private File lastDir = null; 

	public MainPanel() {
		this.nxtText.setLineWrap(true); 
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false); 
		splitPane.setLeftComponent(this.picPanel); 
		splitPane.setRightComponent(new JScrollPane(this.nxtText)); 
		
		splitPane.setResizeWeight(1); 
		
		this.setLayout(new BorderLayout()); 
		this.add(splitPane, BorderLayout.CENTER); 
		
		this.picPanel.addPropertyChangeListener(PicturePanel.IMAGE_UPDATE_PROP, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				MainPanel.this.updateNxtPart(); 
			}
		}); 
	}
	
	protected void updateNxtPart() {
		Dimension size = this.picPanel.getImageSize(); 
		byte[] data = this.picPanel.getNxtImageData(); 
		String text = Converter.getImageCreateString(data, size); 
		this.nxtText.setText(text); 
	}
	
	public boolean setFile(File file) throws IOException {
		BufferedImage image = ImageIO.read(file); 
		if (image != null) {
			this.picPanel.setImage(image); 
			return true; 
		} else {
			return false; 
		}
	}
	
	public void readImage(BufferedImage image) {
		this.picPanel.setImage(image); 
	}
	
	public JMenuBar getMenuBar(final JPanel panel) {
		JMenu menu; 
		JMenuBar menuBar = new JMenuBar(); 
		
		// image menu
		menu = new JMenu("Image"); 
		menu.setMnemonic(KeyEvent.VK_I);
		menuBar.add(menu); 
		
		// open
		Action openFileAction = new AbstractAction("Import Image...") {
			/**SN*/
			private static final long serialVersionUID = -1915349463841717491L;

			@Override
			public void actionPerformed(ActionEvent evt) {
				JFileChooser dialog = new JFileChooser(lastDir); 
				if (dialog.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
					String errorMsg = null; 
					lastDir = dialog.getCurrentDirectory(); 
					try {
						BufferedImage image = ImageIO.read(dialog.getSelectedFile()); 
						if (image == null) {
							errorMsg = "Not an image file or file format not supported. "; 
						} else {
							boolean canReadImage = true; 
							if (image.getWidth() * image.getHeight() > 100 * 64 * 2) {
								String message = "<html>NXT can only display images smaller than <font color='blue'>100x64</font>. <br>" + 
									"But the image you are importing is <font color='red'>" + image.getWidth() + "x" + image.getHeight() + "</font>.<br>" + 
									"Are you sure you want to import this image? <br>" + 
									"(This may also cause a performance issue!)</html>"; 
								canReadImage = JOptionPane.showConfirmDialog(MainPanel.this, message, "Confirm", JOptionPane.YES_NO_OPTION | JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION; 
							}
							if (canReadImage) {
								MainPanel.this.readImage(image); 
							}
						}
					} catch (IOException e) {
						errorMsg = "Error occured when reading file: " + e.getMessage(); 
					} 
					if (errorMsg != null) {
						JOptionPane.showMessageDialog(MainPanel.this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE); 
					}
				}
			}
		}; 
		
		menu.add(openFileAction); 
		menu.addSeparator(); 
		
		// Exit
		Action exitAction = new AbstractAction("Exit") {
			/**SN*/
			private static final long serialVersionUID = -2290105226448425978L;

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0); 
			}
		}; 
		
		menu.add(exitAction); 
		return menuBar; 
	}


}
