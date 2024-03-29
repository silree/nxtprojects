package org.programus.nxj.rockboy.games.bball.ctrls;

import org.programus.nxj.rockboy.core.io.IOModule;
import org.programus.nxj.util.Condition;

/**
 * I had wanted to use this condition to pause game 
 * when the player is not sit in front of his/her NXTRockBoy. 
 * But it does not work well. 
 * 
 * If you have any good idea or fixed this class, please just mail your copy to my mailbox: programus@gmail.com
 * Thanks! :-)
 * @author Programus
 *
 */
public class DistancePauseCondition implements Condition {
	
	private int limitDistance; 
	private int count; 
	private int limitCount; 
	private boolean satisfied; 
	
	private final static IOModule io = IOModule.getIOModule(); 
	
	public DistancePauseCondition() {
		this(100, 10); 
	}
	
	public DistancePauseCondition(int limit, int count) {
		this.limitDistance = limit; 
		this.count = 0; 
		this.limitCount = count; 
		this.satisfied = false; 
		io.getDistance(); 
	}

	@Override
	public boolean isSatisfied() {
		int distance = io.getDistance(); 
		boolean satisfiedThisTime = distance > this.limitDistance; 
		if (satisfiedThisTime != this.satisfied) {
			this.count++; 
		}
		
		if (count > this.limitCount) {
			this.count = 0; 
			this.satisfied = satisfiedThisTime; 
		}
		return this.satisfied;
	}

}
