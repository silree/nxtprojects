package org.programus.nxj.rockboy.games.bball.ctrls;

import lejos.nxt.Button;

import org.programus.nxj.util.Condition;

/**
 * Touch Sensor switch the pause state. 
 * @author Programus
 *
 */
public class ButtonSwitchPauseCondition implements Condition {
	
	private boolean satisfied; 
	private boolean pressedState; 
	private Button pauseButton; 
	
	public ButtonSwitchPauseCondition(Button pauseButton) {
		this.pauseButton = pauseButton; 
	}
	
	@Override
	public boolean isSatisfied() {
		boolean state = this.pauseButton.isPressed(); 
		if (state != this.pressedState) {
			if (state) {
				this.satisfied = !this.satisfied; 
			}
			this.pressedState = state; 
		}
		return this.satisfied;
	}

}
