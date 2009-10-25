package org.programus.nxj.util;

import lejos.nxt.Sound;
import lejos.util.Delay;

public class SoundUtil {
	public static int[][][] NOTE_FREQ = {
		{ // 3
			{262 >> 1, 277 >> 1}, // C
			{294 >> 1, 311 >> 1}, // D
			{330 >> 1}, // E
			{349 >> 1, 370 >> 1}, // F
			{392 >> 1, 415 >> 1}, // G
			{440 >> 1, 466 >> 1}, // A
			{494 >> 1}, // B
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
	}; 
	
	public static void playNote(int pitch, int note, boolean alt, int duration, boolean block) {
		pitch -= 3; 
		if (pitch < 0) {
			pitch = 0; 
		}
		if (pitch >= NOTE_FREQ.length) {
			pitch = NOTE_FREQ.length - 1; 
		}
		note--; 
		if (note < 0) {
			note = 0; 
		}
		if (note >= 7) {
			note = 6; 
		}
		
		int[] freqs = NOTE_FREQ[pitch][note]; 
		int freq = freqs[alt && freqs.length > 1 ? 1 : 0]; 
		
		Sound.playTone(freq, duration); 
		if (block) {
			Delay.msDelay(duration); 
		}
	}
	
	public static void playNote(int[] inst, int pitch, int note, boolean alt, int duration) {
		pitch -= 3; 
		if (pitch < 0) {
			pitch = 0; 
		}
		if (pitch >= NOTE_FREQ.length) {
			pitch = NOTE_FREQ.length - 1; 
		}
		note--; 
		if (note < 0) {
			note = 0; 
		}
		if (note >= 7) {
			note = 6; 
		}
		
		int[] freqs = NOTE_FREQ[pitch][note]; 
		int freq = freqs[alt && freqs.length > 1 ? 1 : 0]; 
		
		Sound.playNote(inst, freq, duration); 
	}
}
