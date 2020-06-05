package com.prototype.app;

public class Coordinates {
	double latitude;
	double longitude;
	double time;
	
	public Coordinates(double time, double latitude, double longitude) {
		this.time = time;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getTime() {
		return time;
	}

}
