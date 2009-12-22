package org.programus.nxj.util;

import lejos.nxt.Sound;
import lejos.util.Delay;

import org.programus.nxj.util.music.Music;
import org.programus.nxj.util.music.MusicNote;

/**
 * Sound utility. A collection of some sound related functions. 
 * @author Programus
 * @see org.programus.nxj.util.music.Music
 * @see org.programus.nxj.util.music.MusicNote
 */
public class SoundUtil {

	/**
	 * Play a music note. It is the same as <code>note.play()</code> method
	 * @param note
	 * @param beatDuration
	 * @param block
	 * @see MusicNote#play()
	 */
	public static void playNote(MusicNote note, int beatDuration, boolean block) {
		note.play(beatDuration, block); 
	}
	
	/**
	 * Play a music note. It is the same as <code>note.play(int, boolean)</code> method
	 * @param note
	 * @param beatDuration
	 * @param vol
	 * @param block
	 * @see MusicNote#play(int, boolean)
	 */
	public static void playNote(MusicNote note, int beatDuration, int vol, boolean block) {
		note.play(beatDuration, vol, block); 
	}
	
	/**
	 * Play a music note. It is the same as <code>note.play(int[], int)</code> method
	 * @param inst
	 * @param note
	 * @param beatDuration
	 * @see MusicNote#play(int[], int)
	 */
	public static void playNote(int[] inst, MusicNote note, int beatDuration) {
		note.play(inst, beatDuration); 
	}
	
	/**
	 * Recommend to use MusicNote class instead. 
	 * @param pitch
	 * @param note
	 * @param alt
	 * @param duration
	 * @param vol
	 * @param block
	 */
	public static void playNote(int pitch, int note, boolean alt, int duration, int vol, boolean block) {
		pitch -= MusicNote.BOTTOM_PITCH; 
		if (pitch < 0) {
			pitch = 0; 
		}
		if (pitch >= MusicNote.NOTE_FREQ.length) {
			pitch = MusicNote.NOTE_FREQ.length - 1; 
		}
		note--; 
		if (note < 0) {
			note = 0; 
		}
		if (note >= 7) {
			note = 6; 
		}
		
		int[] freqs = MusicNote.NOTE_FREQ[pitch][note]; 
		int freq = freqs[alt && freqs.length > 1 ? 1 : 0]; 
		
		Sound.playTone(freq, duration, vol); 
		if (block) {
			Delay.msDelay(duration); 
		}
	}
	
	/**
	 * Recommend to use MusicNote class instead. 
	 * @param pitch
	 * @param note
	 * @param alt
	 * @param duration
	 * @param block
	 */
	public static void playNote(int pitch, int note, boolean alt, int duration, boolean block) {
		playNote(pitch, note, alt, duration, Sound.getVolume(), block); 
	}

	/**
	 * Recommend to use MusicNote class instead. 
	 * @param inst
	 * @param pitch
	 * @param note
	 * @param alt
	 * @param duration
	 */
	public static void playNote(int[] inst, int pitch, int note, boolean alt, int duration) {
		if (inst == null || inst.length < 1) {
			playNote(pitch, note, alt, duration, true); 
			return; 
		}
		
		pitch -= MusicNote.BOTTOM_PITCH; 
		if (pitch < 0) {
			pitch = 0; 
		}
		if (pitch >= MusicNote.NOTE_FREQ.length) {
			pitch = MusicNote.NOTE_FREQ.length - 1; 
		}
		note--; 
		if (note < 0) {
			note = 0; 
		}
		if (note >= 7) {
			note = 6; 
		}
		
		int[] freqs = MusicNote.NOTE_FREQ[pitch][note]; 
		int freq = freqs[alt && freqs.length > 1 ? 1 : 0]; 
		
		Sound.playNote(inst, freq, duration); 
	}
	
	/**
	 * Play a music. Just the same as <code>Music.play(int[], int)</code>.
	 * @param inst
	 * @param music
	 * @param vol
	 * @see Music#play(int[], int)
	 */
	public static void playMusic(int[] inst, Music music, int vol) {
		music.play(inst, vol); 
	}
	
	/**
	 * Play a music. Just the same as <code>Music.play(int[])</code>.
	 * @param inst
	 * @param music
	 * @see Music#play(int[])
	 */
	public static void playMusic(int[] inst, Music music) {
		music.play(inst); 
	}
	
	/**
	 * Play a music. Just the same as <code>Music.play()</code>.
	 * @param music
	 * @see Music#play()
	 */
	public static void playMusic(Music music) {
		music.play(); 
	}
}
