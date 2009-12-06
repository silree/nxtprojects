package org.programus.nxj.util.music;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lejos.nxt.Sound;
import lejos.util.Delay;

public class BGMBox {
	private static BGMBox instance = new BGMBox(); 
	private BGMBox() {}
	public static BGMBox getInstance() {
		return instance; 
	}
	
	private final static Random rand = new Random(); 
	
	private final static int TICK = 300; 

	private int vol = Sound.getVolume() >> 1; 
	private List<Music> musicList = new ArrayList<Music>(); 
	
	private int[] soundEffect = new int[4]; 
	
	private boolean playing; 
	
	public void addMusic(Music music) {
		this.musicList.add(music); 
	}
	
	public void playSound(int freq, int duration, int vol) {
		if (this.isPlaying()) {
			synchronized (this.soundEffect) {
				this.soundEffect[1] = freq; 
				this.soundEffect[2] = duration; 
				this.soundEffect[3] = vol; 
				this.soundEffect[0] = duration > 0 && vol > 0 ? 1 : 0; 
			}
		} else {
			Sound.playTone(freq, duration, vol); 
		}
	}
	
	public synchronized void playLoop(final int delay) {
		if (this.isPlaying()) {
			return; 
		} else {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Delay.msDelay(delay); 
					playing = true; 
					while (isPlaying()) {
						Music music = BGMBox.this.musicList.get(rand.nextInt(musicList.size())); 
						play(music); 
					}
				}
			}); 
			t.setDaemon(true); 
			t.start(); 
		}
	}
	
	public synchronized void stop() {
		this.playing = false; 
	}
	
	public synchronized boolean play(int index) {
		if (this.isPlaying()) {
			return false; 
		} else {
			final Music music = this.musicList.get(index); 
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					playing = true; 
					play(music); 
					playing = false; 
				}
			}); 
			t.setDaemon(true); 
			t.start(); 
			return true; 
		}
	}
	
	private void play(Music music) {
		for (MusicNote note : music.getNotes()) {
			if (!this.isPlaying()) {
				break; 
			}
			this.play(note, music.getParam()); 
		}
	}
	
	private void play(MusicNote note, SheetParam param) {
		int duration = note.getDuration(param); 
		int dur = duration; 
		while (dur > 0) {
			if (!this.isPlaying()) {
				return; 
			}
			int dNote = Math.min(dur, TICK); 
			synchronized(this.soundEffect) {
				if (this.soundEffect[0] > 0) {
					int dSound = Math.min(this.soundEffect[2], TICK); 
					int d = dSound - dNote; 
					if (d >= 0) {
						dNote = 0; 
					} else {
						dNote = -d; 
						this.soundEffect[0] = 0; 
					}
					
					if (dSound > 0) {
						Sound.playTone(this.soundEffect[1], dSound, this.soundEffect[3]); 
						Delay.msDelay(dSound); 
						this.soundEffect[2] -= dSound; 
					}
				}
			}
			if (dNote > 0) {
				if (note.getNote() > 0) {
					Sound.playTone(note.getFreq(param.getPitchOffset()), dNote, this.vol); 
				}
				Delay.msDelay(dNote); 
			}
			dur -= TICK; 
		}
	}

	/**
	 * @return the playing
	 */
	public boolean isPlaying() {
		return playing;
	}
	
	/**
	 * @return the vol
	 */
	public int getVol() {
		return vol;
	}
	
	/**
	 * @param vol the vol to set
	 */
	public void setVol(int vol) {
		this.vol = vol;
	}
}
