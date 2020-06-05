package com.prototype.app;

public class Cell {
		double signal;
		int count;
	public Cell() {
		signal = 0;
		count = 0;
	}
	public double getSignal() {
		return signal;
	}
	public void setSignal(double signal) {
		this.signal = signal;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int cnt) {
		count = cnt;
	}
	public void incCount() {
		count++;
	}
	public void incCount(int i) {
		count = count + i;
	}
}
