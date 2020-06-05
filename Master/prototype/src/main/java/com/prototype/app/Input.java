package com.prototype.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Input {
	FileReader frSignal;
	BufferedReader brSignal;
	FileReader frCoords;
	BufferedReader brCoords;
	ArrayList<Signal> signallist;
	ArrayList<Coordinates> coordinatelist;
	ArrayList<Dataset> dataset;

	public Input() {
		signallist = new ArrayList<Signal>();
		coordinatelist = new ArrayList<Coordinates>();
		dataset = new ArrayList<Dataset>();

	}

	public ArrayList<Dataset> ReadData() throws IOException {
		ReadSignal();
		ReadCoordinates();
		int index;

		for (Signal s : signallist) {
			index = 0;
			for (Coordinates c : coordinatelist) {
				if (s.getTime() == c.getTime()) {
					dataset.add(new Dataset(c.getLatitude(), c.getLongitude(), s.getStrength()));
					break;

				} else {
					if (s.getTime() < c.getTime()) {
						if(index==0) {
							Coordinates low = coordinatelist.get(index);
							Coordinates high = coordinatelist.get(index);
							
							double lat = Functions.latLinInterpolation(low, s.getTime(), high);
							double lon = Functions.lonLinInterpolation(low, s.getTime(), high);
							
							dataset.add(new Dataset(lat, lon, s.getStrength()));
							break;
						}
						Coordinates low = coordinatelist.get(index-1);
						Coordinates high = coordinatelist.get(index);
						
						double lat = Functions.latLinInterpolation(low, s.getTime(), high);
						double lon = Functions.lonLinInterpolation(low, s.getTime(), high);
						
						dataset.add(new Dataset(lat, lon, s.getStrength()));
						break;

					}
				}
				index++;
			}
		}

		return dataset;
	}

	/*
	 * 
	 * public ArrayList<Dataset> ReadData() throws IOException { ReadSignal();
	 * ReadCoordinates(); int index;
	 * 
	 * for (Coordinates e : coordinatelist) { index = 1; for (Signal s : signallist)
	 * {
	 * 
	 * if (e.getTime() == s.getTime()) { dataset.add(new Dataset(e.getLatitude(),
	 * e.getLongitude(), s.getStrength())); break;
	 * 
	 * } else { if (e.getTime() < s.getTime()) { double signal =
	 * Functions.signalLinInterpolation(signallist.get(index - 2), e.getTime(),
	 * signallist.get(index - 1)); dataset.add(new Dataset(e.getLatitude(),
	 * e.getLongitude(), signal)); break;
	 * 
	 * } } index++; } }
	 * 
	 * return dataset; }
	 */
	private void ReadSignal() throws IOException {
		frSignal = new FileReader("src/sim_defaultinacctf.txt");
		brSignal = new BufferedReader(frSignal);
		String string = brSignal.readLine();
		while (string != null) {
			if (Functions.isSignal(string)) {
				signallist.add(Functions.ParseSignal(string));
			}
			string = brSignal.readLine();
		}
		frSignal.close();
		brSignal.close();
	}

	private void ReadCoordinates() throws IOException {
		frCoords = new FileReader("src/sim_defaultinacctf.txt");
		brCoords = new BufferedReader(frCoords);
		String string = brCoords.readLine();
		while (string != null) {
			if (Functions.isCoordinates(string)) {
				coordinatelist.add(Functions.SimParseCoordinates(string));
			}
			string = brCoords.readLine();
		}
		frCoords.close();
		brCoords.close();
	}
/*
	public ArrayList<Dataset> writeSimulation() {
		Cell[][] grid = new Cell[500][500];
		Dataset s0 = new Dataset(60.463078833333334, 5.328972333333334, 0);
		int zeropoint = 250;
		ArrayList<Dataset> datasetsim = new ArrayList<Dataset>();
		Permutation permutation = new Permutation();
		int[] a = { -3, -2, -1, 0, 1, 2, 3 };
		permutation.Permutate(a, a.length, a.length);
		ArrayList<IndexArray> arrayindex = permutation.getResult();
		for (int x = 150; x < 350; x++) {
			for (int z = 150; z < 350; z++) {
				if (x % 3 == 0 && z % 3 == 0) {
					int i;
					int y;

					if (x > 250) {
						i = x - 250;
					} else {
						i = 250 - x;
					}
					if (z > 250) {
						y = z - 250;
					} else {
						y = 250 - z;
					}
					int ax1 = x - 250;
					int ax2 = z - 250;
					ax1 = ax1 * ax1;
					ax2 = ax2 * ax2;
					double dist = Math.sqrt(ax1 + ax2);
					if (dist < 30) {
						if (dist == 0) {
							for (IndexArray ia : arrayindex) {
								if (grid[x + ia.getX()][z + ia.getY()] == null) {
									grid[x + ia.getX()][z + ia.getY()] = new Cell();
									grid[x + ia.getX()][z + ia.getY()].setSignal(200);
									grid[x + ia.getX()][z + ia.getY()].setCount(1);
								} else {
									grid[x + ia.getX()][z + ia.getY()].incCount();
									grid[x + ia.getX()][z + ia.getY()].setSignal(
											Functions.GaussianAverage(grid[x + ia.getX()][z + ia.getY()].getSignal(),
													200, 1, grid[x + ia.getX()][z + ia.getY()].getCount()));
								}
							}
						} else {
							double perc = 1 - (dist * 0.03);
							for (IndexArray ia : arrayindex) {

								if (grid[x + ia.getX()][z + ia.getY()] == null) {
									grid[x + ia.getX()][z + ia.getY()] = new Cell();
									grid[x + ia.getX()][z + ia.getY()].setSignal(200 * perc);
									grid[x + ia.getX()][z + ia.getY()].setCount(1);
								} else {
									grid[x + ia.getX()][z + ia.getY()].incCount();
									grid[x + ia.getX()][z + ia.getY()].setSignal(
											Functions.GaussianAverage(grid[x + ia.getX()][z + ia.getY()].getSignal(),
													200 * perc, 1, grid[x + ia.getX()][z + ia.getY()].getCount()));

								}
							}
						}

					} else {

						for (IndexArray ia : arrayindex) {

							if (grid[x + ia.getX()][z + ia.getY()] == null) {
								grid[x + ia.getX()][z + ia.getY()] = new Cell();
								grid[x + ia.getX()][z + ia.getY()].setSignal(100);
								grid[x + ia.getX()][z + ia.getY()].setCount(1);
							} else {
								grid[x + ia.getX()][z + ia.getY()].incCount();
								grid[x + ia.getX()][z + ia.getY()].setSignal(
										Functions.GaussianAverage(grid[x + ia.getX()][z + ia.getY()].getSignal(), 100,
												1, grid[x + ia.getX()][z + ia.getY()].getCount()));

							}
						}
					}
				}
			}
		}

		for (int i = 0; i < 500; i++) {
			for (int j = 0; j < 500; j++) {

				if (grid[i][j] != null) {
					datasetsim.add(Functions.CoordinatesFromZeroAndDistanceAndAngle(
							Functions.DistanceFromZeroPoint(i, j, zeropoint),
							Functions.AngleFromZeroPoint(i, j, zeropoint), grid[i][j].getSignal(), s0));
				}
			}
		}

		return datasetsim;
	}
*/
}
