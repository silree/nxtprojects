package org.programus.nxj.guessnumber.utils;

import lejos.nxt.Motor;

public class ScoreDisplay {
	private static final Motor EXACT_SCORE_MOTOR = Motor.B;
	private static final Motor EXIST_SCORE_MOTOR = Motor.C; 
	
	private static final int[] SCORES = {
		0, 		// 0
		60, 	// 1
		120, 	// 2
		-120, 	// 3
		-60, 	// 4
		180,	// 0 (alt)
	}; 
	
	private Motor motor; 
	private boolean exactScoreDisplay; 
	private int score;
	
	public ScoreDisplay(boolean isExactScoreDisplay) {
		this.exactScoreDisplay = isExactScoreDisplay; 
		motor = isExactScoreDisplay ? EXACT_SCORE_MOTOR : EXIST_SCORE_MOTOR; 
		motor.resetTachoCount(); 
		motor.setSpeed(Constants.SCORE_SPEED); 
	}
	
	public void displayScore(int score) {
		if (score > SCORES.length - 1) {
			score %= (SCORES.length - 1); 
		}
		if (score != (this.score % (SCORES.length - 1))) {
			int index = score; 
			if (score == 0 && (this.score == 2 || this.score == 3)) {
				index = SCORES.length - 1; 
			}
			this.score = index;
			int angle = this.exactScoreDisplay ? SCORES[index] : -SCORES[index]; 
			motor.rotateTo(angle, true); 
		}
	}
	
	public void reset() {
		motor.rotateTo(0, true); 
	}
	
	public void congratulationWin() {
		new Thread() {
			public void run() {
				motor.setSpeed(720); 
				motor.rotateTo(ScoreDisplay.this.exactScoreDisplay ? 1080 : -1080); 
				motor.resetTachoCount(); 
				motor.setSpeed(Constants.SCORE_SPEED); 
			}
		}.start(); 
	}
}
