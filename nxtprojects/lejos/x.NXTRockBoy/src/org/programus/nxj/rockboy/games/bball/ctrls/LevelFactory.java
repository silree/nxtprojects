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

/**
 * <p>This is a factory to generate levels from level files. </p>
 * <p>
 * The format of a level file:
 * <ul>
 * <li>
 * The first part of a level file is the level title. 
 * </li>
 * <li>
 * The end of the level title is a byte which value is 0x00. 
 * </li>
 * <li>
 * The second part is the initial position of the ball. 
 * 2 bytes: x value and y value in LCD coordinate. No end mark. 
 * </li>
 * <li>
 * The third part is for obstacles. 
 * Each obstacle needs 4 bytes: 2 bytes for top-left point, 2 bytes for width and height. 
 * There is no separator between obstacles. 
 * </li>
 * </ul>
 * </p>
 * <p>
 * This is an example of a level file. 
 * File content in hex is below: 
 * <pre>
 * 43 52 4F 53 53 00 19 05 19 1B 32 0A 2D 07 0A 32
 * </pre>
 * [43 52 4F 53 53] is the ascii of the title - CROSS. 
 * <br />
 * After that, there is a [00], which tell the program this is the end of the title. 
 * <br />
 * [19 05] is the initial position of the ball. 
 * <br />
 * [19 1B 32 0A] is the first obstacle, it is a rectangle (all obstacles are rectangles), 
 * which top-left point is (0x19, 0x1B) and its width is 0x32, height is 0x0A. 
 * <br />
 * [2D 07 0A 32] is the second obstacle, 
 * which top-left point is (0x2D, 0x07) and its width is 0x0A, height is 0x32. 
 * <br />
 * Actually, the example is the second level. 
 * </p>
 * @author Programus
 *
 */
public class LevelFactory {
	public static final String LEVEL_EXT = ".bbl"; 
	
	private BBGame game; 
	private List<File> levelFiles; 
	private Comparator<String> stringComp = new StringComparator(); 
	private Iterator<File> fileIterator; 
	
	public LevelFactory(BBGame game) {
		// get all files
		File[] files = File.listFiles(); 
		// prepare a list to store level files. 
		this.levelFiles = new ArrayList<File>(files.length); 
		// process files one by one
		// add level files into the level file list ordered by file name. 
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
	
	/**
	 * Analysis the next level file and then generate and return the next level. 
	 * @return the next level. 
	 * @throws IOException something is wrong when reading the level file, or the level file format problem. 
	 */
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
		
		// read the ball initial position
		Point p = level.getInitPosition(); 
		p.x = in.read(); 
		p.y = in.read(); 

		// prepare a obstacle list
		List<Rectangle> obstacleList = level.getObstacleList(); 
		// read obstacle rectangles. 
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
