package org.programus.nxj.util;

import lejos.nxt.Sound;
import lejos.util.Delay;

import org.programus.nxj.util.music.Music;
import org.programus.nxj.util.music.MusicNote;

public class SoundUtil {

	public static void playNote(MusicNote note, int beatDuration, boolean block) {
		note.play(beatDuration, block); 
	}
	
	public static void playNote(MusicNote note, int beatDuration, int vol, boolean block) {
		note.play(beatDuration, vol, block); 
	}
	
	public static void playNote(int[] inst, MusicNote note, int beatDuration) {
		note.play(inst, beatDuration); 
	}
	
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
	
	public static void playNote(int pitch, int note, boolean alt, int duration, boolean block) {
		playNote(pitch, note, alt, duration, Sound.getVolume(), block); 
	}

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
	
	public static void playMusic(int[] inst, Music music, int vol) {
		music.play(inst, vol); 
	}
	
	public static void playMusic(int[] inst, Music music) {
		music.play(inst, Sound.getVolume()); 
	}
	
	public static void playMusic(Music music) {
		music.play(); 
	}
}
