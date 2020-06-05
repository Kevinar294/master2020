package com.prototype.app;

public class Dataset {
	double latitude;
	double longitude;
	double signal;
	
	public Dataset(double lat, double lon, double signal) {
		latitude = lat;
		longitude = lon;
		this.signal = signal;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getSignal() {
		return signal;
	}
	

}
