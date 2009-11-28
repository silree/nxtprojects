package org.programus.nxj.rockboy.games.bball.ctrls;

import org.programus.nxj.rockboy.core.io.IOModule;
import org.programus.nxj.util.Condition;

/**
 * Touch Sensor switch the pause state. 
 * @author Programus
 *
 */
public class TouchSwitchPauseCondition implements Condition {
	
	private boolean satisfied; 
	private boolean pressedState; 
	
	private final static IOModule io = IOModule.getIOModule(); 
	
	@Override
	public boolean isSatisfied() {
		boolean state = io.isTouchPressed(); 
		if (state != this.pressedState) {
			if (state) {
				this.satisfied = !this.satisfied; 
			}
			this.pressedState = state; 
		}
		return this.satisfied;
	}

}
