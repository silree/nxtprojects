package org.programus.nxj.pc.util.img.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.programus.nxj.pc.util.img.core.Converter;

public class MainPanel extends JPanel {
	/** SN */
	private static final long serialVersionUID = -2222575532385000674L;
	
	private static final String EXT = "lni"; 
	
	private PicturePanel picPanel = new PicturePanel(); 
	private CodePanel codePanel = new CodePanel(); 
	
	private File lastDir = null; 
	
	private byte[] currData; 
	private Dimension currSize; 

	public MainPanel() {		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false); 
		splitPane.setLeftComponent(this.picPanel); 
		splitPane.setRightComponent(this.codePanel); 
		
		splitPane.setResizeWeight(1); 
		
		this.setLayout(new BorderLayout()); 
		this.add(splitPane, BorderLayout.CENTER); 
		
		this.picPanel.addPropertyChangeListener(PicturePanel.IMAGE_UPDATE_PROP, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				MainPanel.this.updateNxtPart(); 
			}
		}); 
		
		this.codePanel.addPropertyChangeListener(CodePanel.CODE_UPDATE_PROP, new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				MainPanel.this.updateImageFromCode(); 
			}
		}); 
	}
	
	protected void updateNxtPart() {
		currSize = this.picPanel.getImageSize(); 
		currData = this.picPanel.getNxtImageData(); 
		String text = Converter.getImageCreateString(currData, currSize); 
		this.codePanel.setCode(text); 
	}
	
	protected void updateImageFromCode() {
		String code = this.codePanel.getCode(); 
		BufferedImage image = Converter.getImageFromNxtImageCreateString(code); 
		if (image == null) {
			String message = "<html>Code format error!<br />" +
					"Please use format like below:<br />" +
					"<code>new Image(w, h, new byte[] {(byte)0xXX, (byte)0xXX, ...}</code>" +
					"</html>"; 
			JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE); 
		} else {
			this.readImage(image); 
		}
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
	
	protected void saveImage(File file) throws IOException {
		DataOutputStream out = new DataOutputStream(new FileOutputStream(file, false)); 
		try {
			// byte size -> int size. 
//			out.write(this.currSize.width); 
//			out.write(this.currSize.height); 
//			out.write(0); 
			out.writeInt(this.currSize.width); 
			out.writeInt(this.currSize.height); 
			out.writeByte(0); 
			out.write(this.currData); 
		} catch (IOException e) {
			throw e; 
		} finally {
			out.close(); 
		}
	}
	
	protected void readNxtImage(File file) throws IOException {
		DataInputStream in = new DataInputStream(new FileInputStream(file)); 
		int w;
		int h;
		List<Byte> byteList = new LinkedList<Byte>(); 
		try {
			// byte size -> int size. 
//			w = in.read(); 
//			if (w < 0) {
//				throw new IOException("File format error!"); 
//			}
//			h = in.read(); 
//			if (h < 0) {
//				throw new IOException("File format error!"); 
//			}
			try {
				w = in.readInt(); 
				h = in.readInt();
			} catch (EOFException e) {
				throw new IOException("File format error!", e); 
			} 
			int i = in.read(); 
			if (i != 0) {
				throw new IOException("File format error!"); 
			}
			do {
				i = in.read(); 
				if (i >= 0) {
					byteList.add((byte) i); 
				} else {
					break; 
				}
			} while (i >= 0);
		} catch (IOException e) {
			throw e; 
		} finally {
			in.close(); 
		}
		BufferedImage image = Converter.NxtImageData2Image(byteList, w, h); 
		this.readImage(image); 
	}
	
	public JMenuBar getMenuBar(final JPanel panel) {
		JMenu menu; 
		JMenuBar menuBar = new JMenuBar(); 
		
		// image menu
		menu = new JMenu("Image"); 
		menu.setMnemonic(KeyEvent.VK_I);
		menuBar.add(menu); 
		
		// Import
		Action importFileAction = new AbstractAction("Import Image...") {
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
		
		menu.add(importFileAction); 
		menu.addSeparator(); 
		
		// Open lni (LeJOS NXT Image) file
		Action openFileAction = new AbstractAction("Open LeJOS NXT Image File...") {
			/**SN*/
			private static final long serialVersionUID = 3458676330985853465L;

			@Override
			public void actionPerformed(ActionEvent evt) {
				JFileChooser dialog = new JFileChooser(lastDir); 
				dialog.setFileFilter(new FileNameExtensionFilter("LeJOS NXT Image File (*." + EXT + ")", EXT)); 
				dialog.setAcceptAllFileFilterUsed(false); 
				if (dialog.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
					lastDir = dialog.getCurrentDirectory(); 
					File file = dialog.getSelectedFile(); 
					try {
						MainPanel.this.readNxtImage(file);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(panel, "<html>Error occured when reading file.<br><font color='red'>" + e.getMessage() + "</font></html>", "Error", JOptionPane.ERROR_MESSAGE); 
					} 
				}
			}
		}; 
		
		menu.add(openFileAction); 
		
		// Export
		final Action exportFileAction = new AbstractAction("Export LeJOS NXT Image File...") {
			/**SN*/
			private static final long serialVersionUID = 3458676330985853465L;

			@Override
			public void actionPerformed(ActionEvent evt) {
				JFileChooser dialog = new JFileChooser(lastDir); 
				dialog.setFileFilter(new FileNameExtensionFilter("LeJOS NXT Image File (*." + EXT + ")", EXT)); 
				dialog.setAcceptAllFileFilterUsed(false); 
				if (dialog.showSaveDialog(panel) == JFileChooser.APPROVE_OPTION) {
					lastDir = dialog.getCurrentDirectory(); 
					File file = dialog.getSelectedFile(); 
					if (file.exists()) {
						if (JOptionPane.showConfirmDialog(panel, "File exists. Overwrite?", "Confirm", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
							return; 
						}
					}
					try {
						if (!file.exists()) {
							String fname = file.getPath(); 
							if (!fname.endsWith("." + EXT)) {
								if (!fname.endsWith(".")) {
									fname += "."; 
								}
								file = new File(fname + EXT); 
							}
							file.createNewFile(); 
						}
						if (!file.canWrite()) {
							JOptionPane.showMessageDialog(panel, "File cannot be written!", "Error", JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION); 
							return; 
						}
						MainPanel.this.saveImage(file);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(panel, "<html>Error occured when write data into file.<br><font color='red'>" + e.getMessage() + "</font></html>", "Error", JOptionPane.ERROR_MESSAGE); 
						e.printStackTrace(); 
					} 
				}
			}
		}; 
		
		menu.add(exportFileAction); 

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
		
		menu.addMenuListener(new MenuListener() {
			@Override
			public void menuCanceled(MenuEvent e) {
			}
			@Override
			public void menuDeselected(MenuEvent e) {
			}
			@Override
			public void menuSelected(MenuEvent e) {
				exportFileAction.setEnabled(MainPanel.this.currData != null && MainPanel.this.currSize != null); 
			}
		}); 
		
		return menuBar; 
	}


}
