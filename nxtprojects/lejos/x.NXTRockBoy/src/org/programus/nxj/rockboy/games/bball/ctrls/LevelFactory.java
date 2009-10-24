package org.programus.nxj.rockboy.games.bball.ctrls;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.programus.nxj.util.Comparator;
import org.programus.nxj.util.StringComparator;

public class LevelFactory {
	public static final String LEVEL_EXT = ".bbl"; 
	
	private BBGame game; 
	private List<File> levelFiles; 
	private Comparator<String> stringComp = new StringComparator(); 
	private Iterator<File> fileIterator; 
	
	public LevelFactory(BBGame game) {
		File[] files = File.listFiles(); 
		this.levelFiles = new ArrayList<File>(files.length); 
		for (File f : files) {
			if (f != null) {
				String name = f.getName(); 
				if (name.lastIndexOf(LEVEL_EXT) == name.length() - LEVEL_EXT.length()) {
					int index = 0; 
					for (int i = this.levelFiles.size() - 1; i >= 0; i--) {
						if (stringComp.compare(this.levelFiles.get(i).getName(), name) < 0) {
							index = i + 1; 
							break; 
						}
					}
					this.levelFiles.add(index, f); 
				}
			}
		}
		this.game = game; 
		this.reset(); 
	}
	
	public void reset() {
		this.fileIterator = this.levelFiles.iterator(); 
	}
	
	public GameLevel getNextLevel() throws IOException {
		if (!this.fileIterator.hasNext()) {
			return null; 
		}
		File file = this.fileIterator.next(); 
		FileInputStream in = new FileInputStream(file); 
		GameLevel level = new GameLevel(this.game); 
		int byteData = 0; 
		// read title. 
		StringBuffer titleBuffer = new StringBuffer(); 
		do {
			byteData = in.read(); 
			if (byteData > 0) {
				titleBuffer.append((char) byteData); 
			}
		} while (byteData > 0); 
		if (byteData < 0) {
			throw new UnsupportedEncodingException("File Format not correct."); 
		}
		level.setTitle(titleBuffer.toString()); 
		
		Point p = level.getInitPosition(); 
		p.x = in.read(); 
		p.y = in.read(); 

		List<Rectangle> obstacleList = level.getObstacleList(); 
		
		while(true) {
			byteData = in.read(); 
			if (byteData < 0) {
				break; 
			}
			Rectangle rect = new Rectangle(); 
			rect.x = byteData; 
			byteData = in.read(); 
			if (byteData < 0) {
				break; 
			}
			rect.y = byteData; 
			byteData = in.read(); 
			if (byteData < 0) {
				break; 
			}
			rect.width = byteData; 
			byteData = in.read(); 
			if (byteData < 0) {
				break; 
			}
			rect.height = byteData; 
			obstacleList.add(rect); 
		}
		in.close(); 
		
		return level; 
	}
}
