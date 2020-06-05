package com.prototype.app;

import java.util.ArrayList;

public class GridManager {
	int height = 500;
	int breadth = 500;
	Cell[][] grid;
	Dataset s0;
	int zeropoint = 250;

	public GridManager(double latzero, double lonzero) {
		grid = new Cell[breadth][height];
		s0 = new Dataset(latzero, lonzero, 0);
	}

	public void setDataset(ArrayList<Dataset> dataset) {
		int x;
		int y;
		double weight;
		double signal;
		double dist;
		
		Permutation permutation = new Permutation();
		int[] a = {-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5};
		permutation.Permutate(a, a.length, a.length);
		ArrayList<IndexArray> arrayindex = permutation.getResult();
		
		for (Dataset c : dataset) {
			x = Functions.GridXValueOfCoordinate(s0, c);
			y = Functions.GridYValueOfCoordinate(s0, c);
			
			for (IndexArray ia : arrayindex) {
				
				if(ia.getX() == 0 && ia.getY() == 0) {
					signal = c.getSignal();
					weight = 1000;
				}else {
					dist = Math.sqrt(Math.pow(ia.getX(), 2)+Math.pow(ia.getY(), 2));
					signal = c.getSignal();
					weight = 1000/Math.pow(dist, 2);
				}
				
				if (grid[zeropoint + x + ia.getX()][zeropoint + y + ia.getY()] == null) {
					grid[zeropoint + x + ia.getX()][zeropoint + y + ia.getY()] = new Cell();
					grid[zeropoint + x + ia.getX()][zeropoint + y + ia.getY()].setSignal(signal);
					grid[zeropoint + x + ia.getX()][zeropoint + y + ia.getY()].setCount((int)weight);
				} else {
					grid[zeropoint + x + ia.getX()][zeropoint + y + ia.getY()].incCount((int)weight);
					grid[zeropoint + x + ia.getX()][zeropoint + y + ia.getY()].setSignal(Functions.GaussianAverage(
							grid[zeropoint + x + ia.getX()][zeropoint + y + ia.getY()].getSignal(), signal, weight,
							grid[zeropoint + x + ia.getX()][zeropoint + y + ia.getY()].getCount()));
				}
			}
		}
	}

	public ArrayList<Dataset> getDataset() {
		ArrayList<Dataset> dataset = new ArrayList<Dataset>();

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < breadth; j++) {

				if (grid[i][j] != null) {
					dataset.add(Functions.CoordinatesFromZeroAndDistanceAndAngle(
							Functions.DistanceFromZeroPoint(i, j, zeropoint),
							Functions.AngleFromZeroPoint(i, j, zeropoint), grid[i][j].getSignal(), s0));
				}
			}
		}

		return dataset;
	}
}
