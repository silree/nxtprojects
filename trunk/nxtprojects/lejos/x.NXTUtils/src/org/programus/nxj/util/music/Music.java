package org.programus.nxj.util.music;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lejos.nxt.Sound;


public class Music {
	private SheetParam param; 
	private MusicNote[] notes;
	
	public Music(SheetParam param, MusicNote[] notes) {
		this.param = param; 
		this.notes = notes; 
	}
	
	public Music(SheetParam param, String[] sheets) {
		this.param = param; 
		this.readSheet(sheets); 
	}
	
	public void readSheet(String[] sheets) {
		List<MusicNote> noteList = new ArrayList<MusicNote>(); 
//		this.notes = new MusicNote[sheet.length() / 5]; 
		for (String sheet : sheets) {
			for (StringTokenizer token = new StringTokenizer(sheet, " "); token.hasMoreTokens(); ) {
				String s = token.nextToken(); 
				if (s.length() > 0 && !" ".equals(s)) {
					noteList.add(new MusicNote(s)); 
				}
			}
		}
		this.notes = new MusicNote[noteList.size()]; 
		int i = 0; 
		for (MusicNote note : noteList) {
			this.notes[i++] = note; 
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		if (this.notes != null) {
			for (MusicNote note : this.notes) {
				sb.append(note.toString()).append(' '); 
			}
		}
		
		return sb.toString(); 
	}
	
	public void play(int[] inst, int vol) {
		for (MusicNote note : this.notes) {
			if (inst != null && inst.length > 0) {
				note.play(inst, this.param.getBeatDuration(), this.param.getPitchOffset()); 
			} else {
				note.play(this.param.getBeatDuration(), this.param.getPitchOffset(), vol, true); 
			}
		}
	}
	
	public void play(int[] inst) {
		this.play(inst, Sound.getVolume()); 
	}
	
	public void play(int vol) {
		this.play(null, vol); 
	}
	
	public void play() {
		this.play(null, Sound.getVolume()); 
	}
	
	/**
	 * @param param the param to set
	 */
	public void setParam(SheetParam param) {
		this.param = param;
	}
	
	/**
	 * @return the param
	 */
	public SheetParam getParam() {
		return param;
	}

	/**
	 * @return the notes
	 */
	public MusicNote[] getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(MusicNote[] notes) {
		this.notes = notes;
	} 
}
