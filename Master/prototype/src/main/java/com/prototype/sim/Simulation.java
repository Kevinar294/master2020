package com.prototype.sim;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;


import com.prototype.app.Dataset;
import com.prototype.app.Functions;

public class Simulation {

	double[][] grid;
	Dataset s0;
	int zeropoint;
	ArrayList<Dataset> datasetsim;
	double M;
	double multiplier;

	public Simulation() {
		grid = new double[500][500];
		s0 = new Dataset(60.463078833333334, 5.328972333333334, 0);
		zeropoint = 250;
		datasetsim = new ArrayList<Dataset>();
		M = 7;
		
	}

	public void simDefault() throws IOException {
		double dist;
		double xvalue;
		double yvalue;
		double zvalue = 10;
		double angle;
		for (int x = 175; x < 325; x++) {
			for (int y = 175; y < 325; y++) {
				if(x%6==0 && y%6==0) {
				if (x == zeropoint) {
					xvalue = 0;
				} else if (x > zeropoint) {
					xvalue = x - zeropoint;
				} else {
					xvalue = zeropoint - x;
				}

				if (y == zeropoint) {
					yvalue = 0;
				} else if (y > zeropoint) {
					yvalue = y - zeropoint;
				} else {
					yvalue = zeropoint - y;
				}

				dist = Math.sqrt(Math.pow(xvalue, 2)+Math.pow(yvalue, 2)+Math.pow(zvalue, 2));
				
				double a = (0*xvalue)+(0*yvalue)+(1*zvalue);
				double b = Math.sqrt(Math.pow(xvalue, 2)+Math.pow(yvalue, 2)+Math.pow(zvalue, 2));
				
				angle = Math.acos(a/b);
				grid[x][y] = M/(2*Math.PI*Math.pow(dist, 3))*(Math.sqrt(1+3*(Math.pow(Math.cos(angle),2))));
				}
			}
		}
	for(int x = 175; x<325; x++) {
		for(int y = 175; y<325; y++) {
			if(grid[x][y]!=0) {
				datasetsim.add(Functions.CoordinatesFromZeroAndDistanceAndAngle(
						getDistWithInaccuracy(x, y, zeropoint),
						Functions.AngleFromZeroPoint(x, y, zeropoint), grid[x][y], s0));
			}
		}
	}
	double time = 0;
	FileWriter fr = new FileWriter("src/sim_defaultinacctf.txt");
	BufferedWriter bw = new BufferedWriter(fr);
	//FileWriter fr2 = new FileWriter("src/topydef45.txt");
	//BufferedWriter bw2 = new BufferedWriter(fr2);
	Random random = new Random();
	double variation;
	double variation2;
	for(Dataset e : datasetsim) {
		time++;
		variation = random.nextInt(5);
		variation2 = random.nextInt(5);
		
		//bw2.write(String.format("%.12f", e.getSignal())+",\n");
		bw.write(time+variation/10+","+time+",$OXSIG,"+String.format("%.12f", e.getSignal())+",***"+"\n");
		bw.write(time+variation2/10+","+time+",$GPGGA,******.**,"+e.getLatitude()+",x,"+e.getLongitude()+",x,,x,x,x,x,x,x,x,x,x"+"\n");
		
	}
	bw.flush();
	bw.close();
	//bw2.flush();
	//bw2.close();
	}
	
	private double getDistWithInaccuracy(int x, int y, int zeropoint) {
		Random random = new Random();
		double distance = random.nextInt(26)/10;
		double distance2 = random.nextInt(26)/10;
		double angle = random.nextInt(360);
		double angle2 = random.nextInt(360);
		
		double xres = x + distance*Math.cos(Math.toRadians(angle))+distance2*Math.cos(Math.toRadians(angle2));
		double yres = y + distance*Math.sin(Math.toRadians(angle))+distance2*Math.sin(Math.toRadians(angle2));

		
		return Functions.DistanceFromZeroPoint(xres, yres, zeropoint);
	}

}
