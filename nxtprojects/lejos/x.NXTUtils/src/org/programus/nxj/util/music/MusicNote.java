package org.programus.nxj.util.music;

import lejos.nxt.Sound;
import lejos.util.Delay;

/**
 * <p>
 * MusicNote class represents a note in music. 
 * A note has its pitch (music scale), name, and duration. 
 * </p>
 * <p>
 * <div>In one pitch there are 7 note names - C, D, E, F, G, A, B or 1, 2, 3, 4, 5, 6, 7 for numbered musical notation. 
 * Besides these 7 names, there is another special name - 0 which represents rest. </div>
 * <div>MusicNote class support the pitches from 0 to 9. </div>
 * </p>
 * <p>
 * There is another aspect of note name is the # and b mark. MusicNote support only # mark. Db is the same as C#. 
 * </p>
 * <p>
 * The duration in MusicNote class is not an absolute number of second or microsecond. Instead, it is a rate of the beatDuration. 
 * A beatDuration is the time length of whole note. The beatDuration information will be stored in SheetParam object. 
 * </p>
 * <p>
 * <div>You can create a MusicNote object by specify all the parameters above as well as by a note description string. </div>
 * <div>A note description string have a format below:</div>
 * <div>nX[#]/d1/d2...</div>
 * <ul>
 * <li>n - pitch</li>
 * <li>X - note name (0, C, D, ... , A, B or 0, 1, 2, 3, ... , 6, 7)</li>
 * <li># - add this if it is a note with # mark</li>
 * <li>/dn - duration. dn is the denominator. /2 means half note, /4 means quarter note, etc. 
 * You can use the format like /4/8 to represent a quarter dotted note. </li>
 * </ul>
 * <div>For example, </div>
 * <div>4C/4 or 41/4 means a pitch 4, C (do), quarter note. </div>
 * <div>4C#/2/4 means a pitch 4, C#, dotted half note. </div>
 * </p>
 * @author Programus
 * @see SheetParam
 *
 */
public class MusicNote {
	public static int BOTTOM_PITCH = 0; 
	public static int[][][] NOTE_FREQ = {
		{ // 0
			{16, 17}, // C
			{18, 19}, // D
			{21}, // E
			{22, 23}, // F
			{25, 26}, // G
			{28, 29}, // A
			{31}, // B
		}, 
		{ // 1
			{33, 35}, // C
			{37, 39}, // D
			{41}, // E
			{44, 46}, // F
			{49, 52}, // G
			{55, 58}, // A
			{62}, // B
		}, 
		{ // 2
			{65, 69}, // C
			{73, 78}, // D
			{82}, // E
			{87, 92}, // F
			{98, 104}, // G
			{110, 117}, // A
			{123}, // B
		}, 
		{ // 3
			{131, 139}, // C
			{147, 156}, // D
			{165}, // E
			{175, 185}, // F
			{196, 208}, // G
			{220, 233}, // A
			{247}, // B
		}, 
		{ // 4
			{262, 277}, // C
			{294, 311}, // D
			{330}, // E
			{349, 370}, // F
			{392, 415}, // G
			{440, 466}, // A
			{494}, // B
		}, 
		{ // 5
			{523, 554}, // C
			{587, 622}, // D
			{659}, // E
			{698, 740}, // F
			{784, 831}, // G
			{880, 932}, // A
			{988}, // B
		}, 
		{ // 6
			{1047, 1109}, // C
			{1175, 1245}, // D
			{1319}, // E
			{1397, 1480}, // F
			{1568, 1661}, // G
			{1760, 1865}, // A
			{1976}, // B
		}, 
		{ // 7
			{2093, 2217}, // C
			{2349, 2489}, // D
			{2637}, // E
			{2794, 2960}, // F
			{3136, 3322}, // G
			{3520, 3729}, // A
			{3951}, // B
		}, 
		{ // 8
			{4186, 4435}, // C
			{4699, 4978}, // D
			{5274}, // E
			{5588, 5920}, // F
			{6272, 6644}, // G
			{7040, 7458}, // A
			{7902}, // B
		}, 
		{ // 9
			{8372, 8870}, // C
			{9397, 9956}, // D
			{10548}, // E
			{11175, 11840}, // F
			{12544, 13290}, // G
			{14080, 14917}, // A
			{15804}, // B
		}, 
	};

	private static char[] NOTE_CHAR = {
		'0', 'C', 'D', 'E', 'F', 'G', 'A', 'B',
	}; 
	private static char DURATION_DELIMIT = '/'; 
	private static char ALT_CHAR = '#'; 
	
	private int pitch; 
	private int note; 
	private boolean alt;
	private int[] duration; 
	
	/**
	 * Construction of MusicNote. 
	 * @param pitch
	 * @param note
	 * @param alt
	 */
	public MusicNote(int pitch, int note, boolean alt) {
		this.setPitch(pitch); 
		this.setNote(note); 
		this.setAlt(alt); 
	}
	
	/**
	 * Construction of MusicNote. 
	 * @param note note description string
	 */
	public MusicNote(String note) {
		this(note.toCharArray()); 
	}
	
	/**
	 * Construction of MusicNote. 
	 * @param note note description string characters
	 */
	public MusicNote(char[] note) {
		this(note, 0, note.length); 
	}
	
	/**
	 * Construction of MusicNote. 
	 * @param note
	 * @param start
	 * @param end
	 */
	public MusicNote(char[] note, int start, int end) {
		int i = start; 
		end = Math.min(end, note.length); 
		this.setPitch(note[i++] - '0'); 
		char c = note[i++]; 
		if (c < 'A') {
			// numbered note
			this.setNote(c - '0'); 
		} else {
			if (c > 'Z') {
				// to upper case
				c -= 32; 
			}
			c -= 'C'; 
			if (c > 0xff00) {
				c += (NOTE_CHAR.length - 1); 
			}
			this.setNote(c + 1); 
		}
		c = note[i++]; 
		if (c == ALT_CHAR) {
			this.setAlt(c == ALT_CHAR); 
			c = note[i++]; 
		}
		int[] da = new int[(end - i + 1) >> 1]; 
		int j = 0; 
		while (i < end) {
			int n = 0; 
			for (c = note[i++]; c != DURATION_DELIMIT; c = note[i++]) {
				n = n * 10 + c - '0'; 
				if (i >= end) {
					break; 
				}
			}
			int k = 0; 
			while ((1 << k) < n) {
				k++; 
			}
			da[j++] = k; 
		}
		if (j < da.length) {
			this.duration = new int[j]; 
			System.arraycopy(da, 0, this.duration, 0, j); 
		} else {
			this.duration = da; 
		}
	}
	
	/**
	 * Return frequency of this note. 
	 * @return frequency of this note
	 */
	public int getFreq() {
		return this.getFreq(0); 
	}
	
	/**
	 * Return frequency of this note after offset pitch
	 * @param pitchOffset
	 * @return frequency of this note after offset pitch
	 */
	public int getFreq(int pitchOffset) {
		int p = this.pitch + pitchOffset; 
		if (p < 0) {
			p = 0; 
		} else if (p >= NOTE_FREQ.length) {
			p = NOTE_FREQ.length - 1; 
		}
		int[] freqs = NOTE_FREQ[p][note]; 
		return freqs[freqs.length > 1 && alt ? 1 : 0]; 
	}
	
	/**
	 * Return the note duration in second
	 * @param beatDuration duration for a beat or a whole note
	 * @return the note duration in second
	 */
	public int getDuration(int beatDuration) {
		int dur = 0; 
		if (this.duration != null) {
			for (int d : this.duration) {
				dur += (beatDuration >> d); 
			}
		}
		return dur; 
	}
	
	/**
	 * Return the note duration in second
	 * @param p param for sheet music
	 * @return the note duration in second
	 */
	public int getDuration(SheetParam p) {
		return this.getDuration(p.getBeatDuration()); 
	}
	
	/**
	 * Return the pitch of this note
	 * @return the pitch
	 */
	public int getPitch() {
		return pitch + BOTTOM_PITCH;
	}
	/**
	 * @param pitch the pitch to set
	 */
	public void setPitch(int pitch) {
		this.pitch = pitch - BOTTOM_PITCH;
		if (this.pitch < 0) {
			this.pitch = 0; 
		} else if (this.pitch >= NOTE_FREQ.length) {
			this.pitch = NOTE_FREQ.length - 1; 
		}
	}
	/**
	 * Return the note as integer. The returned integer value is the number of this note in the numbered musical notation
	 * @return the note
	 */
	public int getNote() {
		return note + 1;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(int note) {
		this.note = note - 1;
		if (note < 0) {
			note = -1; 
		} else if (this.note > 6) {
			this.note = 6; 
		}
	}
	/**
	 * @return true if this is a note with # mark
	 */
	public boolean isAlt() {
		return alt;
	}
	/**
	 * @param alt the alt to set
	 */
	public void setAlt(boolean alt) {
		this.alt = alt;
	}
	
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int[] duration) {
		this.duration = duration;
	}

	/**
	 * @return the duration
	 */
	public int[] getDuration() {
		return duration;
	}
	
	/**
	 * Play this note using the specified instrument. 
	 * This method will block current thread. 
	 * @param inst instrument
	 * @param beatDuration beat duration
	 * @see Sound#PIANO
	 * @see Sound#XYLOPHONE
	 * @see Sound#FLUTE
	 */
	public void play(int[] inst, int beatDuration) {
		int dur = this.getDuration(beatDuration); 
		if (this.note < 0) {
			Delay.msDelay(dur); 
		} else {
			if (inst != null && inst.length > 0) {
				Sound.playNote(inst, this.getFreq(), dur); 
			} else {
				Sound.playTone(this.getFreq(), dur); 
				Delay.msDelay(dur); 
			}
		}
	}
	
	/**
	 * Play this note after pitch offset using the specified instrument. 
	 * This method will block current thread. 
	 * @param inst instrument
	 * @param beatDuration beat duration
	 * @param pitchOffset
	 * @see Sound#PIANO
	 * @see Sound#XYLOPHONE
	 * @see Sound#FLUTE
	 */
	public void play(int[] inst, int beatDuration, int pitchOffset) {
		int dur = this.getDuration(beatDuration); 
		if (this.note < 0) {
			Delay.msDelay(dur); 
		} else {
			if (inst != null && inst.length > 0) {
				Sound.playNote(inst, this.getFreq(pitchOffset), dur); 
			} else {
				Sound.playTone(this.getFreq(pitchOffset), dur); 
				Delay.msDelay(dur); 
			}
		}
	}
	
	/**
	 * Play this note after pitch offset using the specified volume. 
	 * @param beatDuration beat duration
	 * @param pitchOffset
	 * @param vol
	 * @param block specify whether you want this method to block
	 */
	public void play(int beatDuration, int pitchOffset, int vol, boolean block) {
		int dur = this.getDuration(beatDuration); 
		if (this.note >= 0) {
			Sound.playTone(this.getFreq(pitchOffset), dur, vol); 
		}
		if (block) {
			Delay.msDelay(dur); 
		}
	}

	/**
	 * Play this note. Please reference other play method help. 
	 * @param beatDuration
	 * @param vol
	 * @param block
	 */
	public void play(int beatDuration, int vol, boolean block) {
		this.play(beatDuration, 0, vol, block); 
	}
	
	/**
	 * Play this note. Please reference other play method help. 
	 * @param beatDuration
	 * @param vol
	 */
	public void play(int beatDuration, int vol) {
		this.play(beatDuration, vol, true); 
	}
	
	/**
	 * Play this note. Please reference other play method help. 
	 * @param beatDuration
	 * @param block
	 */
	public void play(int beatDuration, boolean block) {
		this.play(beatDuration, Sound.getVolume(), block); 
	}
	
	/**
	 * Play this note. Please reference other play method help. 
	 * @param beatDuration
	 */
	public void play(int beatDuration) {
		this.play(beatDuration, true); 
	}
	
	/**
	 * Play this note. Please reference other play method help. 
	 */
	public void play() {
		this.play(SheetParam.NORMAL_BEAT_DUR); 
	}

	/**
	 * Return the note description string of this note. 
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		sb.append(this.pitch + BOTTOM_PITCH); 
		sb.append(NOTE_CHAR[this.getNote()]); 
		if (this.alt) {
			sb.append(ALT_CHAR); 
		}
		for (int d : duration) {
			sb.append(DURATION_DELIMIT).append(1 << d); 
		}
		
		return sb.toString(); 
	}
}
