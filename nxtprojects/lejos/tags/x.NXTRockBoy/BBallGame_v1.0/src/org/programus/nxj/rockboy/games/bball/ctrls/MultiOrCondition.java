package org.programus.nxj.rockboy.games.bball.ctrls;

import java.util.ArrayList;
import java.util.List;

import org.programus.nxj.util.Condition;

/**
 * Union more than one conditions. The logic operator is OR. 
 * @author Programus
 *
 */
public class MultiOrCondition implements Condition {
	private List<Condition> conditions; 
	
	public MultiOrCondition() {
		this.conditions = new ArrayList<Condition>(); 
	}
	
	public MultiOrCondition(Condition[] conditions) {
		this(); 
		this.addConditions(conditions); 
	}
	
	public MultiOrCondition(List<Condition> conditions) {
		this(); 
		this.addConditions(conditions); 
	}
	
	public void addConditions(Condition[] conditions) {
		for (Condition c : conditions) {
			this.addCondition(c); 
		}
	}
	
	public void addConditions(List<Condition> conditions) {
		for (Condition c : conditions) {
			this.addCondition(c); 
		}
	}
	
	public void addCondition(Condition condition) {
		if (!this.conditions.contains(condition)) {
			this.conditions.add(condition); 
		}
	}
	
	@Override
	public boolean isSatisfied() {
		boolean ret = false; 
		for (Condition c : this.conditions) {
			if (c.isSatisfied()) {
				ret = true; 
				break; 
			}
		}
		return ret;
	}

}
