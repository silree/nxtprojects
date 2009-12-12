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
	
	private final static int EXT = 10; 
	private final static int DEFAULT_TICK = 100; 
	
	private boolean pause; 
	
	private int volRate = 50; 
	private int oldSysVol; 
	private int vol; 
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
		SheetParam param = music.getParam(); 
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
		for (MusicNote note : music.getNotes()) {
			if (!this.isPlaying()) {
				break; 
			}
			
			while(this.isPause()) {
				Delay.msDelay(tick); 
			}
			
			int vol = this.getVol(); 
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
				if (!this.isPlaying()) {
					return; 
				}
				int playTime = 0; 
				int dNote = Math.min(dur, aTick); 
				synchronized(this.soundEffect) {
					if (this.soundEffect[0] > 0) {
						int dSound = Math.min(this.soundEffect[2], aTick); 
						int d = dSound - dNote; 
						if (d >= 0) {
							dNote = 0; 
						} else {
							dNote = -d; 
							this.soundEffect[0] = 0; 
						}
						
						if (dSound > 0) {
							Sound.playTone(this.soundEffect[1], dSound + EXT, this.soundEffect[3]); 
							Delay.msDelay(dSound); 
							playTime += dSound; 
							this.soundEffect[2] -= dSound; 
						}
					}
				}
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

	/**
	 * @return the playing
	 */
	public boolean isPlaying() {
		return playing;
	}
	
	public int getVol() {
		if (this.oldSysVol != Sound.getVolume()) {
			this.vol = Sound.getVolume() * this.volRate / 100; 
			this.oldSysVol = Sound.getVolume(); 
		}
		return this.vol; 
	}
	
	/**
	 * @return the volRate
	 */
	public int getVolRate() {
		return volRate;
	}
	
	/**
	 * @param volRate the volRate to set
	 */
	public void setVolRate(int vol) {
		this.volRate = vol;
	}
	/**
	 * @param pause the pause to set
	 */
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	/**
	 * @return the pause
	 */
	public boolean isPause() {
		return pause;
	}
}
