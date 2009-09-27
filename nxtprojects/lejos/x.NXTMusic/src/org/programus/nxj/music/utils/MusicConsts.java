package org.programus.nxj.music.utils;


public class MusicConsts {
	public static int MIN_DURATION = 500; 
//	public static int[][] INSTS = {
//		Sound.PIANO, 
//		Sound.FLUTE, 
//		Sound.XYLOPHONE, 
//		null, // original electric sound
//	}; 
//	public static String[] INST_NAMES = {
//		"PIANO", 
//		"FLUTE", 
//		"XYLOPHONE", 
//		"Electric Sound", 
//	}; 
	public static int[][][] NOTE_FREQ = {
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
}
