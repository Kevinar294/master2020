/**
 * Copyright 2019 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.prototype.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.esri.arcgisruntime.geometry.CoordinateFormatter;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.Grid;
import com.esri.arcgisruntime.mapping.view.LatitudeLongitudeGrid;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.MgrsGrid;
import com.esri.arcgisruntime.mapping.view.UsngGrid;
import com.esri.arcgisruntime.mapping.view.UtmGrid;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol.Style;
import com.esri.arcgisruntime.symbology.SimpleRenderer;

public class App extends Application {

	private MapView mapView;

	// private static final SpatialReference SPATIAL_REFERENCE =
	// SpatialReferences.getWgs84();
	public static void main(String[] args) {

		Application.launch(args);
	}

	@Override
	public void start(Stage stage) {

		// set the title and size of the stage and show it
		stage.setTitle("Prototype");
		stage.setWidth(800);
		stage.setHeight(700);
		stage.show();

		// create a JavaFX scene with a stack pane as the root node and add it to the
		// scene
		StackPane stackPane = new StackPane();
		Scene scene = new Scene(stackPane);
		stage.setScene(scene);

		// create a MapView to display the map and add it to the stack pane
		mapView = new MapView();
		stackPane.getChildren().add(mapView);

		// create an ArcGISMap with the default imagery basemap
		ArcGISMap map = new ArcGISMap(Basemap.Type.IMAGERY, 60.463078833333334, 5.328972333333334, 20);

		// Gridtesting
		Grid gridLl = new LatitudeLongitudeGrid();
		Grid gridUtm = new UtmGrid();
		Grid gridUsng = new UsngGrid();
		Grid gridMgrs = new MgrsGrid();

		// display the map by setting the map on the map view
		mapView.setMap(map);
	//	mapView.setGrid(gridLl);

		// Graphics
		GraphicsOverlay graphicsoverlay = new GraphicsOverlay();
		mapView.getGraphicsOverlays().add(graphicsoverlay);

		
		Input input = new Input();
		try {
			ArrayList<Dataset> dataset = input.ReadData();
			
			//dataset = Functions.RemoveNoiseByConstant(dataset,0.0004);
			GridManager gridmanager = new GridManager(dataset.get(0).getLatitude(), dataset.get(0).getLongitude());
			gridmanager.setDataset(dataset);
			Dataset topleft, topright, lowleft, lowright;
			PointCollection collection;
			Polygon polygon;
			Graphic polygongraphic;
			int i = 0;
			for (Dataset d : gridmanager.getDataset()) {
		//	for(Dataset d: input.writeSimulation()) {
				i++;
				//System.out.println(d.getSignal());
				/*
				 * Graphic graphic = new Graphic(d.getLatitude(), d.getLongitude());
				 * graphic.setSymbol(greencircle); graphicsoverlay.getGraphics().add(graphic);
				 */

				topleft = Functions.CoordinatesFromZeroAndDistanceAndAngle(0.7, 315, 0, d);
				topright = Functions.CoordinatesFromZeroAndDistanceAndAngle(0.7, 45, 0, d);
				lowleft = Functions.CoordinatesFromZeroAndDistanceAndAngle(0.7, 135, 0, d);
				lowright = Functions.CoordinatesFromZeroAndDistanceAndAngle(0.7, 225, 0, d);

				collection = new PointCollection(SpatialReferences.getWgs84());
				collection.add(CoordinateFormatter.fromLatitudeLongitude(
						topleft.getLatitude() + "," + topleft.getLongitude(), SpatialReferences.getWgs84()));
				collection.add(CoordinateFormatter.fromLatitudeLongitude(
						topright.getLatitude() + "," + topright.getLongitude(), SpatialReferences.getWgs84()));
				collection.add(CoordinateFormatter.fromLatitudeLongitude(
						lowleft.getLatitude() + "," + lowleft.getLongitude(), SpatialReferences.getWgs84()));
				collection.add(CoordinateFormatter.fromLatitudeLongitude(
						lowright.getLatitude() + "," + lowright.getLongitude(), SpatialReferences.getWgs84()));
				polygon = new Polygon(collection);
				if (d.getSignal() > 0.0016) {
					polygongraphic = new Graphic(polygon,
							new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x80FF0000, null));
				} else if (d.getSignal() > 0.0012) {
					polygongraphic = new Graphic(polygon,
							new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x80FF2E00, null));
				} else if (d.getSignal() > 0.0010) {
					polygongraphic = new Graphic(polygon,
							new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x80FF4200, null));
				} else if (d.getSignal() > 0.0008) {
					polygongraphic = new Graphic(polygon,
							new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x80FF5D00, null));
				} else if (d.getSignal() > 0.0006) {
					polygongraphic = new Graphic(polygon,
							new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x80FF8300, null));
				} else if (d.getSignal() > 0.0004) {
					polygongraphic = new Graphic(polygon,
							new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x80FFA200, null));
				}
				else if (d.getSignal() > 0.0002) {
					polygongraphic = new Graphic(polygon,
							new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x60FFD100, null));
				}else {
					polygongraphic = new Graphic(polygon,
							new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x4000FF00, null));
				}

				graphicsoverlay.getGraphics().add(polygongraphic);
			}

			/*
			 * for(Dataset d:dataset) {
			 * 
			 * Graphic graphic = new Graphic(d.getLatitude(), d.getLongitude());
			 * graphic.setSymbol(redcircle); graphicsoverlay.getGraphics().add(graphic); }
			 */
			System.out.println("Sizenew:" + i + " Sizeold:" + dataset.size());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Stops and releases all resources used in application.
	 */
	@Override
	public void stop() {

		if (mapView != null) {
			mapView.dispose();
		}
	}
}
