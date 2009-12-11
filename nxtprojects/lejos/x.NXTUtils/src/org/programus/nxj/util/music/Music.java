package org.programus.nxj.util.music;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lejos.nxt.Sound;
import lejos.util.Delay;


public class Music {
	public final static int DEFAULT_TICK = 100; 
	
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
				this.play(vol); 
			}
		}
	}
	
	public void play(int[] inst) {
		this.play(inst, Sound.getVolume()); 
	}
	
	public void play(int vol) {
		final int EXT = 10; 
		int[] beatStyle = param.getBeatStyle(); 
		int[] dvs; 
		int oldVol = 0; 
		int tick; 
		if (null == beatStyle || beatStyle.length < 2) {
			tick = DEFAULT_TICK; 
			dvs = new int[1]; 
		} else {
			tick = param.getBeatDuration() / beatStyle.length; 
			dvs = new int[beatStyle.length]; 
		}
		
		int aTick = tick; 
		int index = 0; 
		for (MusicNote note : this.getNotes()) {
			// calculate beat volume. 
			if (oldVol != vol && dvs.length > 1) {
				oldVol = vol; 
				for (int i = 0; i < dvs.length; i++) {
					dvs[i] = oldVol * beatStyle[i] / 100; 
				}
			}
			// play every note. 
			int duration = note.getDuration(param); 
			int muteTime = tick >> 5; 
			if (!param.isContinueNote()) {
				duration -= muteTime; 
			}
			int dur = duration; 
			while (dur > 0) {
				int playTime = 0; 
				int dNote = Math.min(dur, aTick); 
				if (dNote > 0) {
					if (note.getNote() > 0) {
						Sound.playTone(note.getFreq(param.getPitchOffset()), dNote + EXT, vol + dvs[index]); 
					}
					Delay.msDelay(dNote); 
					playTime += dNote; 
				}
				dur -= aTick; 
				aTick -= playTime; 
				if (aTick <= 0) {
					aTick = tick; 
					index++; 
					if (index >= dvs.length) {
						index = 0; 
					}
				}
			}
			
			if (!param.isContinueNote()) {
				Sound.playTone(0, 1, 0); 
				Delay.msDelay(muteTime); 
			}
		}
	}
	
	public void play() {
		this.play(Sound.getVolume()); 
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
