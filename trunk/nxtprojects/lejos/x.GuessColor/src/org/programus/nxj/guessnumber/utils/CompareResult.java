package org.programus.nxj.guessnumber.utils;

public class CompareResult {
	public int exactCorrect; 
	public int existCorrect;
	
	public void displayScore(ScoreDisplay exactDisplay, ScoreDisplay existDisplay) {
		exactDisplay.displayScore(exactCorrect); 
		existDisplay.displayScore(existCorrect); 
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(); 
		sb.append(exactCorrect).append('A').append(existCorrect).append('B'); 
		return sb.toString(); 
	}
}
