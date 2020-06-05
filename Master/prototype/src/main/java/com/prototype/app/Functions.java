package com.prototype.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Functions {

	
	public static double signalLinInterpolation(Signal lowsignal, double time, Signal highsignal) {
		double x = time;
		double x0 = lowsignal.getTime();
		double x1 = highsignal.getTime();
		double y0 = lowsignal.getStrength();
		double y1 = highsignal.getStrength();
		return y0 + (y1 - y0) * ((x - x0) / (x1 - x0));
	}

	public static double latLinInterpolation(Coordinates low, double time, Coordinates high) {
		double x = time;
		double x0 = low.getTime();
		double x1 = high.getTime();
		double y0 = low.getLatitude();
		double y1 = high.getLatitude();
		return y0 + (y1 - y0) * ((x - x0) / (x1 - x0));
	}
	
	public static double lonLinInterpolation(Coordinates low, double time, Coordinates high) {
		double x = time;
		double x0 = low.getTime();
		double x1 = high.getTime();
		double y0 = low.getLongitude();
		double y1 = high.getLongitude();
		return y0 + (y1 - y0) * ((x - x0) / (x1 - x0));
	}
	
	public static boolean isSignal(String string) {
		int identifier = string.indexOf('$');
		if (string.substring(identifier + 1, identifier + 6).equals("OXSIG")) {
			return true;
		}
		return false;
	}

	public static boolean isCoordinates(String string) {
		int identifier = string.indexOf('$');
		if (string.substring(identifier + 1, identifier + 6).equals("GPGGA")) {
			return true;
		}
		return false;
	}

	public static Signal ParseSignal(String string) {
		String[] data = string.split("\\,");
		double time = Double.parseDouble(data[0]);
		double strength = Double.parseDouble(data[3]);
		return new Signal(time, strength);
	}

	public static double ConvertLatGPPGAtoDD(String string) {
		double degree = Double.parseDouble(string.substring(0, 2));
		double minutes = Double.parseDouble(string.substring(2));
		return degree + (minutes / 60);
	}

	public static double ConvertLonGPPGAtoDD(String string) {
		double degree = Double.parseDouble(string.substring(0, 3));
		double minutes = Double.parseDouble(string.substring(3));
		return degree + (minutes / 60);
	}

	public static Coordinates ParseCoordinates(String string) {
		String[] data = string.split("\\,");
		double time = Double.parseDouble(data[0]);
		double lat = Functions.ConvertLatGPPGAtoDD(data[4]);
		double lon = Functions.ConvertLonGPPGAtoDD(data[6]);
		return new Coordinates(time, lat, lon);
	}
	
	public static Coordinates SimParseCoordinates(String string) {
		String[] data = string.split("\\,");
		double time = Double.parseDouble(data[0]);
		double lat = Double.parseDouble(data[4]);
		double lon = Double.parseDouble(data[6]);
		return new Coordinates(time, lat, lon);
	}
	
	public static ArrayList<Dataset> RemoveNoiseByConstant(ArrayList<Dataset> dataset, double limit){
		ArrayList<Dataset> result = new ArrayList<Dataset>();
		for(Dataset c : dataset) {
			if(c.getSignal()>limit) {
				result.add(c);
			}
		}
		return result;
	}
	public static ArrayList<Dataset> RemoveNoiseFrequencyBased(ArrayList<Dataset> dataset) {
		Double margin = 1.15;
		ArrayList<Double> signallist = new ArrayList<Double>();

		for (Dataset c : dataset) {
			signallist.add(c.getSignal());
		}

		HashMap<Integer, Integer> freq = new HashMap<Integer, Integer>();
		int v;
		for(double c:signallist) {
			v = (int) c;
			if(freq.containsKey(v)){
				freq.put(v, freq.get(v)+1);
			}else {
				freq.put(v, 1);
			}
		}
		Integer max = 0;
		Double value = 0.0;
		for(Map.Entry<Integer, Integer> entry:freq.entrySet()) {
			
			if(entry.getValue()>max) {
				max = entry.getValue();
				value = (double)entry.getKey();
			}
		}
		
		//OutputDataTestingOnly(freq);		
		
		ArrayList<Dataset> result = new ArrayList<Dataset>();
		for (Dataset c : dataset) {
		
			if (c.getSignal() > (value * margin)) {
				
				result.add(c);
			}
		}
		return result;
	}

	private static void OutputDataTestingOnly(Map<Integer, Integer> frequency) {
		Map<Integer, Integer> freq = new TreeMap<Integer, Integer>();
		Integer sum = 0;
		freq.putAll(frequency);
		System.out.print("X:");
		for(Map.Entry<Integer, Integer> entry:freq.entrySet()) {
			System.out.print(entry.getKey()+",");
		}
		System.out.print("\nY:");
		for(Map.Entry<Integer, Integer> entry:freq.entrySet()) {
			sum = sum+entry.getValue();
			System.out.print(entry.getValue()+",");
		}
		System.out.println("\nSum"+sum);
	}
	
	public static ArrayList<Coordinates> ShredDuplicateCoordinates(ArrayList<Coordinates> coords) {
		// TODO
		return coords;

	}

	public static double DistanceBetweenTwoCoordinates(Dataset s0, Dataset s) {
		int r = 6371000;
		double lat0 = Math.toRadians(s0.getLatitude());
		double lat = Math.toRadians(s.getLatitude());
		double deltalat = Math.toRadians(s.getLatitude() - s0.getLatitude());
		double deltalon = Math.toRadians(s.getLongitude() - s0.getLongitude());

		double a = Math.sin(deltalat / 2) * Math.sin(deltalat / 2)
				+ Math.cos(lat0) * Math.cos(lat) * Math.sin(deltalon / 2) * Math.sin(deltalon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return r * c;

	}

	public static double AngleBetweenTwoCoordinates(Dataset s0, Dataset s) {
		double lat0 = Math.toRadians(s0.getLatitude());
		double lat = Math.toRadians(s.getLatitude());
		double lon0 = Math.toRadians(s0.getLongitude());
		double lon = Math.toRadians(s.getLongitude());

		double y = Math.sin(lon - lon0) * Math.cos(lat);
		double x = Math.cos(lat0) * Math.sin(lat) - Math.sin(lat0) * Math.cos(lat) * Math.cos(lon - lon0);
		return Math.toDegrees(Math.atan2(y, x));
	}

	public static int GridXValueOfCoordinate(Dataset s0, Dataset s) {
		double angle = Functions.AngleBetweenTwoCoordinates(s0, s);
		double hypotenuse = Functions.DistanceBetweenTwoCoordinates(s0, s);
		double xvalue = hypotenuse * Math.sin(Math.toRadians(angle));
		return (int) Math.round(xvalue);
		
	}

	public static int GridYValueOfCoordinate(Dataset s0, Dataset s) {
		double angle = Functions.AngleBetweenTwoCoordinates(s0, s);
		double hypotenuse = Functions.DistanceBetweenTwoCoordinates(s0, s);
		double yvalue = hypotenuse * Math.cos(Math.toRadians(angle));
		return (int) Math.round(yvalue);
	}
	public static double DistanceFromZeroPoint(double x, double y, int zeropoint) {
		if(x<zeropoint) {
			x = x - zeropoint;
		}else {
			x = zeropoint - x;
		}
		if(y<zeropoint) {
			y = y - zeropoint;
		}else {
			y = zeropoint - y;
		}
		return Math.sqrt((x*x)+(y*y));
	}
	public static double AngleFromZeroPoint(double x, double y, int zeropoint) {
		if(x == zeropoint){
			if(y<=zeropoint) {
				return 180;
			}else {
				return 0;
			}
			
		}
		if(y == zeropoint) {
			if(x>=zeropoint) {
				return 90;
			}else {
				return 270;
			}
		}
		x = x-zeropoint;
		y = y-zeropoint;
		double angle = Math.toDegrees(Math.atan2((double)x,(double)y));
		return angle;
	}
	
	public static Dataset CoordinatesFromZeroAndDistanceAndAngle(double distance, double angle, double strength, Dataset s0) {
		int r = 6371000;
		double lat0 = Math.toRadians(s0.getLatitude());
		double lat = Math.asin(Math.sin(lat0)*Math.cos(distance/r)+Math.cos(lat0)*Math.sin(distance/r)*Math.cos(Math.toRadians(angle)));
		double lon = Math.toRadians(s0.getLongitude()) + Math.atan2(Math.sin(Math.toRadians(angle))*Math.sin(distance/r)*Math.cos(lat0),Math.cos(distance/r)-Math.sin(lat0)*Math.sin(lat));;
		return new Dataset(Math.toDegrees(lat), Math.toDegrees(lon), strength);
	}
	
	public static double GaussianAverage(double value, double input, double weight, int count) {
		double p = weight/count;
		return input*p+(1-p)*value;
	}

	
}
