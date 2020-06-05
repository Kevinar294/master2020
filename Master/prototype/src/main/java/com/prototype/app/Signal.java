package com.prototype.app;

public class Signal {
	double strength;
	double time;

	public Signal(double time, double strength) {
		this.time = time;
		this.strength = strength;
	}
	
	public double getStrength() {
		return strength;
	}
	public double getTime() {
		return time;
	}
}
