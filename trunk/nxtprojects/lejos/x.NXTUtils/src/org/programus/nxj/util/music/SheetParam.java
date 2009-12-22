package org.programus.nxj.util.music;

/**
 * SheetParam class is a normal java bean to store the miscellaneous parameters of a music. 
 * The parameters contains: 
 * <ul>
 * <li>beatDuration - Beat Duration, time duration of a whole note or a beat. </li>
 * <li>beatStyle - Beat Style, the strength of each note in a beat. 
 * It is an int[], and a beat would be separated into <code><i>beatStyle.length</i></code> parts, 
 * and each part has its own volume value. The volume value will be standard volume + beatStyle[i]. 
 * This could implement the strength control of notes. </li>
 * <li>pitchOffset - Pitch Offset, set this none-zero when you want to play a music in a higher or lower pitch. </li>
 * <li>continueNote - Continue Note, when it is true, note sequence like "4C/4 4C/8 4C/4" would sound just like "4C/4/8/4" or "4C/2/8", 
 * otherwise there would be a very short rest between the three "4C/x"s. </li>
 * </ul>
 * @author Programus
 * @see MusicNote
 * @see Music
 *
 */
public class SheetParam {
	/**
	 * Slow Beat Duration. 
	 */
	public static final int SLOW_BEAT_DUR = 2000; 
	/**
	 * Normal Beat Duration. 
	 */
	public static final int NORMAL_BEAT_DUR = 1000; 
	/**
	 * Fast Beat Duration. 
	 */
	public static final int FAST_BEAT_DUR = 500; 
	
	/**
	 * Standard 4/4 time signature
	 * Sounds like <b>one</b> <i>two</i> three <i>four</i> ...
	 */
	public static final int[] STD_44 = {20, 0, 10, 0}; 
	/**
	 * Standard 2/4 time signature
	 * Sounds like <b>one</b> <i>two</i> <b>one</b> <i>two</i> ...
	 */
	public static final int[] STD_24 = {20, 0, 20, 0}; 
	
	private int beatDuration = NORMAL_BEAT_DUR; 
	private int[] beatStyle = STD_44; 
	private int pitchOffset; 
	private boolean continueNote; 
	
	/**
	 * Constructor
	 * @param beatDuration
	 * @param pitchOffset
	 * @param beatStyle
	 * @param continueNote
	 */
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
