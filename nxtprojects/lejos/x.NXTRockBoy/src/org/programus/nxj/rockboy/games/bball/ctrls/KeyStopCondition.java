package org.programus.nxj.rockboy.games.bball.ctrls;

import lejos.nxt.Button;

import org.programus.nxj.util.Condition;

public class KeyStopCondition implements Condition {
	private Button stopButton; 
	
	public KeyStopCondition(Button stopButton) {
		this.stopButton = stopButton; 
	}

	@Override
	public boolean isSatisfied() {
		if (this.stopButton != null) {
			return this.stopButton.isPressed(); 
		} else {
			int status = Button.readButtons(); 
			return status != 0; 
		}
	}

}
