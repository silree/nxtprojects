package org.programus.nxj.util.music;

public class SheetParam {
	public static final int SLOW_BEAT_DUR = 2000; 
	public static final int NORMAL_BEAT_DUR = 1000; 
	public static final int FAST_BEAT_DUR = 500; 
	
	private int beatDuration = NORMAL_BEAT_DUR; 
	private int[] beatStyle = {10, 0, 5, 0}; 
	private int pitchOffset; 
	
	public SheetParam(int beatDuration, int pitchOffset) {
		this.beatDuration = beatDuration; 
		this.pitchOffset = pitchOffset; 
	}
	
	/**
	 * @return the beatDuration
	 */
	public int getBeatDuration() {
		return beatDuration;
	}
	/**
	 * @param beatDuration the beatDuration to set
	 */
	public void setBeatDuration(int beatDuration) {
		this.beatDuration = beatDuration;
	}
	/**
	 * @return the beatStyle
	 */
	public int[] getBeatStyle() {
		return beatStyle;
	}
	/**
	 * @param beatStyle the beatStyle to set
	 */
	public void setBeatStyle(int[] beatStyle) {
		this.beatStyle = beatStyle;
	}
	/**
	 * @param pitchOffset the pitchOffset to set
	 */
	public void setPitchOffset(int pitchOffset) {
		this.pitchOffset = pitchOffset;
	}
	/**
	 * @return the pitchOffset
	 */
	public int getPitchOffset() {
		return pitchOffset;
	}
}
