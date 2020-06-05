package com.prototype.app;

public class IndexArray {

	private int x;
	private int y;
	
	public IndexArray(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	@Override
    public boolean equals(Object o) {
        if(o instanceof IndexArray) {
        	if(x == ((IndexArray)o).getX() && y == ((IndexArray)o).getY()) {
        		return true;
        	}
        }
        return false;
    }
}
