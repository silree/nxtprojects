package org.programus.nxj.util.music;

public class SheetParam {
	public static final int SLOW_BEAT_DUR = 2000; 
	public static final int NORMAL_BEAT_DUR = 1000; 
	public static final int FAST_BEAT_DUR = 500; 
	public static final int[] STD_44 = {20, 0, 10, 0}; 
	public static final int[] STD_24 = {20, 0, 20, 0}; 
	
	private int beatDuration = NORMAL_BEAT_DUR; 
	private int[] beatStyle = STD_44; 
	private int pitchOffset; 
	private boolean continueNote; 
	
	public SheetParam(int beatDuration, int pitchOffset, int[] beatStyle, boolean continueNote) {
		this.beatDuration = beatDuration; 
		this.pitchOffset = pitchOffset; 
		this.beatStyle = beatStyle; 
		this.setContinueNote(continueNote); 
	}
	
	public SheetParam() {}
	
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

	/**
	 * @param continueNote the continueNote to set
	 */
	public void setContinueNote(boolean continueNote) {
		this.continueNote = continueNote;
	}

	/**
	 * @return the continueNote
	 */
	public boolean isContinueNote() {
		return continueNote;
	}
}
