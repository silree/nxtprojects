package org.programus.nxj.rockboy.core.thing;

import org.programus.nxj.rockboy.core.World;
import org.programus.nxj.rockboy.core.mc.McVector;

public class DropCalculator {
	private Thing master; 
	private McVector g; 
	private double dropRate; 
	
	public DropCalculator(Thing t) {
		this(t, 1); 
	}
	
	public DropCalculator(Thing t, double dropRate) {
		this.master = t; 
		this.dropRate = dropRate; 
		this.g = new McVector(); 
		g.setStaticInLcd(false); 
		g.setNaturalVector(World.G * dropRate, -90); 
	}
	
	public double getDropRate() {
		return this.dropRate; 
	}
	
	public void updateDropSpeed() {
		this.master.speed.add(this.g); 
	}
}
