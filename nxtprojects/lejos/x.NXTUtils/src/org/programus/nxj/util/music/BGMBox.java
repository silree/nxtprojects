package org.programus.nxj.util.music;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lejos.nxt.Sound;
import lejos.util.Delay;

/**
 * BGMBox is the abbreviation of Background Music Box. It is a background music playing engine. 
 * <p>Normally, you use it like below:</p>
 * <div>First, initialize a BGMBox for your system</div>
 * <code><pre>
 * BGMBox box = BGMBox.getInstance(); 
 * box.setVolRate(50); 
 * for (Music music : musics) { // musics is a collection of musics. 
 *   box.addMusic(music); 
 * }
 * </pre></code>
 * <div>Then, call playLoop() method to play and stop method to stop(). setPause() method allow you to temporary pause the music. </div>
 * <code><pre>
 * BGMBox box = BGMBox.getInstance(); 
 * box.playLoop(0); 
 * // other code...
 * boolean pause = .....; 
 * box.setPause(pause); 
 * // other code...
 * box.stop(); 
 * </pre></code>
 * <div>If you want to play some other sound effect, use playSound() method of BGMBox instead of Sound class. </div>
 * <code><pre>
 * BGMBox box = BGMBox.getInstance(); 
 * box.playSound(4000, 10, 100); // the parameters are just the same as Sound.playTone() method. 
 * </pre></code>
 * @author Programus
 * @see Music
 * @see Sound
 * @see Sound#playTone(int, int, int)
 *
 */
public class BGMBox {
	private static BGMBox instance = new BGMBox(); 
	private BGMBox() {}
	/**
	 * Get a BGMBox instance. BGMBox using a singleton design, so there is always the same instance. 
	 * @return a BGMBox instance.
	 */
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
	
	/**
	 * Add a new music. 
	 * @param music
	 */
	public void addMusic(Music music) {
		this.musicList.add(music); 
	}
	
	/**
	 * Clear all musics. 
	 */
	public void clearMusic() {
		this.musicList.clear(); 
	}
	
	/**
	 * Play a sound. This will temporary interrupt the music and the music will continue after the sound is played. 
	 * @param freq
	 * @param duration
	 * @param vol
	 * @see Sound#playTone(int, int, int)
	 */
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
	
	/**
	 * Play all musics after specified delay time. Musics will be played shuffled. 
	 * @param delay the music will begin after the delay time (ms). 
	 */
	public synchronized void playLoop(final int delay) {
		this.playLoop(delay, true); 
	}
	
	/**
	 * Play all musics after specified delay time. 
	 * @param delay the music will begin after the delay time (ms). 
	 * @param shuffled specify the musics will be played shuffled or not. 
	 */
	public synchronized void playLoop(final int delay, final boolean shuffled) {
		if (this.isPlaying()) {
			return; 
		} else {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					int index = 0; 
					Delay.msDelay(delay); 
					playing = true; 
					while (isPlaying()) {
						if (shuffled) {
							index = rand.nextInt(musicList.size()); 
						} else {
							index++; 
							if (index >= BGMBox.this.musicList.size()) {
								index = 0; 
							}
						}
						Music music = BGMBox.this.musicList.get(index); 
						play(music); 
					}
				}
			}); 
			t.setDaemon(true); 
			t.start(); 
		}
	}
	
	/**
	 * Stop play. 
	 */
	public synchronized void stop() {
		this.playing = false; 
	}
	
	/**
	 * Play a particular music by specify its index. 
	 * @param index
	 * @return true if the music begin playing, false if there is another music is playing. 
	 */
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
	 * Indicate whether the background musics is playing. 
	 * @return the playing
	 */
	public boolean isPlaying() {
		return playing;
	}
	
	/**
	 * Return the volume for background music. 
	 * @return the volume for background music. 
	 */
	public int getVol() {
		if (this.oldSysVol != Sound.getVolume()) {
			this.vol = Sound.getVolume() * this.volRate / 100; 
			this.oldSysVol = Sound.getVolume(); 
		}
		return this.vol; 
	}
	
	/**
	 * Return the volume rate for the background music. The background music will be played with volume = volRate * system volume / 100. 
	 * @return the volRate
	 */
	public int getVolRate() {
		return volRate;
	}
	
	/**
	 * Set the volume rate. It is a percentage value. 
	 * The background music will be played with volume = volRate * system volume / 100. 
	 * @param volRate the volRate to set
	 */
	public void setVolRate(int volRate) {
		this.volRate = volRate;
	}
	/**
	 * Pause or continue the background music. 
	 * @param pause set true if you want to pause the background music, otherwise set false to continue. 
	 */
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	/**
	 * Indicate whether the background music is paused. 
	 * @return the pause
	 */
	public boolean isPause() {
		return pause;
	}
}
