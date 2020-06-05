package com.prototype.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import com.prototype.app.Coordinates;
import com.prototype.app.Dataset;
import com.prototype.app.Functions;
import com.prototype.app.IndexArray;
import com.prototype.app.Permutation;
import com.prototype.app.Signal;

class FunctionsTest {

	@Test
	void testLinInterpolation() {
		assertEquals(10, Functions.signalLinInterpolation(new Signal(5,5), 10, new Signal(15,15)));
	}

	@Test
	void testisSignal(){
		String signal = "2.340,7,$OXSIG,75,*48";
		String coordinates = "2.105,5,$GPGGA,193710.00,6027.78481,N,00519.73818,E,1,05,1.54,100.6,M,43.8,M,,*55";
		assertTrue(Functions.isSignal(signal));
		assertFalse(Functions.isSignal(coordinates));
	}
	@Test
	void testisCoordinates(){
		String signal = "2.340,7,$OXSIG,75,*48";
		String coordinates = "2.105,5,$GPGGA,193710.00,6027.78481,N,00519.73818,E,1,05,1.54,100.6,M,43.8,M,,*55";
		assertFalse(Functions.isCoordinates(signal));
		assertTrue(Functions.isCoordinates(coordinates));
	}
	
	@Test
	void testParseSignal() {
		String input = "2.340,7,$OXSIG,75,*48";
		Signal signal = Functions.ParseSignal(input);
		assertEquals(signal.getTime(), 2.340);
		assertEquals(signal.getStrength(), 75);
	}
	
	@Test
	void testConvertGPPGAtoDD() {
		String lat = "4124.2028";
		String lon = "00210.4418";
		assertEquals(41.40338, Functions.ConvertLatGPPGAtoDD(lat));
		assertEquals(2.17403, Functions.ConvertLonGPPGAtoDD(lon));
	}
	
	@Test
	void testParseCoordinates() {
		String input = "2.105,5,$GPGGA,193710.00,6027.78481,N,00519.73818,E,1,05,1.54,100.6,M,43.8,M,,*55";
		Coordinates coords = Functions.ParseCoordinates(input);
		assertEquals(coords.getTime(), 2.105);
		assertEquals(coords.getLatitude(), Functions.ConvertLatGPPGAtoDD("6027.78481"));
		assertEquals(coords.getLongitude(), Functions.ConvertLonGPPGAtoDD("00519.73818"));
	}
	@Test
	void testRemoveNoiseFrequencyBased() {
		ArrayList<Dataset> list = new ArrayList<Dataset>();
		Dataset ds1 = new Dataset(0,0,101);
		Dataset ds2 = new Dataset(0,0,100);
		Dataset ds3 = new Dataset(0,0,100);
		Dataset ds4 = new Dataset(0,0,105);
		Dataset ds5 = new Dataset(0,0,150);
		list.add(ds1);
		list.add(ds2);
		list.add(ds3);
		list.add(ds4);
		list.add(ds5);
		ArrayList<Dataset> result = Functions.RemoveNoiseFrequencyBased(list);
		assertEquals(result.get(0).getSignal(), ds5.getSignal());
	}
	@Test
	void testDistanceBetweenTwoCoordinates() {
		Dataset ds1 = new Dataset(60.305980, 5.239855,0);
		Dataset ds2 = new Dataset(60.305847, 5.264364,0);
		assertEquals((int)Functions.DistanceBetweenTwoCoordinates(ds1,ds2),1350);
	}
	
	@Test
	void testAngleBetweenTwoCoordinates() {
		Dataset ds1 = new Dataset(60, 5,0);
		Dataset ds2 = new Dataset(61, 5,0);
		Dataset ds3 = new Dataset(61, 5,0);
		Dataset ds4 = new Dataset(60, 5,0);
		assertEquals(Functions.AngleBetweenTwoCoordinates(ds1, ds2),0);
		assertEquals(Functions.AngleBetweenTwoCoordinates(ds3, ds4),180);
	}
	
	@Test
	void testGridXValueOfCoordinate() {
		Dataset ds1 = new Dataset(61,5,0);
		Dataset ds2 = new Dataset(60,5,0);
		Dataset ds3 = new Dataset(60,5.05,0);
		Dataset ds4 = new Dataset(60.005, 5.005,1);
		
		assertEquals(Functions.GridXValueOfCoordinate(ds1, ds2),0);
		assertEquals(Functions.GridXValueOfCoordinate(ds2, ds3),(int)Math.round(Functions.DistanceBetweenTwoCoordinates(ds2,ds3)));
		assertEquals(Functions.GridXValueOfCoordinate(ds2, ds4),278);
		assertEquals(Functions.GridXValueOfCoordinate(ds4, ds2),-278);

	}
	@Test
	void testGridYValueOfCoordinate() {
		Dataset ds1 = new Dataset(60,5,0);
		Dataset ds2 = new Dataset(61,5,0);
		Dataset ds3 = new Dataset(61,5.005,0);
		Dataset ds4 = new Dataset(60.005, 5.005,1);
		
		assertEquals(Functions.GridYValueOfCoordinate(ds1, ds2),(int)Math.round(Functions.DistanceBetweenTwoCoordinates(ds1,ds2)));
		assertEquals(Functions.GridYValueOfCoordinate(ds2, ds3),0);
		assertEquals(Functions.GridYValueOfCoordinate(ds1, ds4),556);
		assertEquals(Functions.GridYValueOfCoordinate(ds4, ds1), -556);

	}
	@Test
	void testDistanceFromZeroPoint() {
		double x = 3;
		double y = 4;
		assertEquals(Functions.DistanceFromZeroPoint(x, y,0),5);
	}
	/*@Test
	void testAngleFromZeroPoint() {
		assertEquals(Math.round(Functions.AngleFromZeroPoint(250, 250, 250)),0);
		assertEquals(Math.round(Functions.AngleFromZeroPoint(250, 0, 250)),0);
		assertEquals(Math.round(Functions.AngleFromZeroPoint(250, 500, 250)),180);
		assertEquals(Math.round(Functions.AngleFromZeroPoint(0, 250, 250)),270);
		assertEquals(Math.round(Functions.AngleFromZeroPoint(500, 250, 250)),90);
		assertEquals(Math.round(Functions.AngleFromZeroPoint(499, 251, 250)),90);
		assertEquals(Math.round(Functions.AngleFromZeroPoint(1, 251, 250)),270);
}*/
	@Test
	void testGaussianAverage() {
		assertEquals(Functions.GaussianAverage(10, 11, 1, 2),10.5);
	}
	
	@Test
	void testPermutation() {
		Permutation permutation = new Permutation();
		int a[] = {-2,-1,0,1,2};
		permutation.Permutate(a, a.length, a.length);
		Iterator<IndexArray> iterator = permutation.getResult().iterator();
		IndexArray ia;
		while(iterator.hasNext()) {
			ia = iterator.next();
			System.out.println(ia.getX()+", "+ia.getY());
		}
	}
	/*
	@Test
	void notATest() {
		Dataset dataset = new Dataset(60.463110669318205, 5.328972333333334, 15);
		Dataset tmp = Functions.CoordinatesFromZeroAndDistanceAndAngle(3.54, 90, 15, dataset);
		assertEquals(tmp.getLatitude(), tmp.getLongitude());
		
	}
	
	@Test
	void notATest2() {
		assertEquals(Functions.DistanceBetweenTwoCoordinates(new Dataset(60.463110669318205, 5.328972333333334, 15), new Dataset(60.463110669318205, 5.328972333396661, 15)), 15);
	}
	*/
}
