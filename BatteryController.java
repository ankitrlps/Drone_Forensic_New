package battery;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opencsv.CSVReader;

import application.Parameters;
import application.TestController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class BatteryController {

	private FileChooser fileChooser = new FileChooser();
	private String filePath;
	private CSVReader reader = null;
	
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Text droneType;
	
	private static final String DroneTypeName = "Yuneec Typhoon H";
	private static final int type1 = 5;
	@FXML
	private void upload(ActionEvent event) {

		fileChooser.setTitle("choose a File");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			filePath = file.getAbsolutePath();
			// fileName = file.getName();
			readRPY(file);
		} else {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setTitle("Error");
			errorAlert.setHeaderText("Error Information");
			errorAlert.setContentText("Error while selection");
			errorAlert.show();
		}
	}

	private void readRPY(File file) {
		try {
			reader = new CSVReader(new FileReader(filePath), ',', '\'', 2);

			String[] column = null;
			// String[] splitDateTime = null;
			List<Parameters> paramValues = new ArrayList<Parameters>();
			while ((column = reader.readNext()) != null) {
				Parameters params = new Parameters(column[0], column[1], column[2], column[3], column[4], column[5],
						column[6], column[7], String.valueOf(column[8]), column[9], column[10], column[11], column[12], column[13],
						column[14], column[15], column[16], column[17], column[18], column[19], column[20], column[21]);

				paramValues.add(params);
			}
			plot(paramValues);
		} catch (IOException io) {
			Logger.getLogger(TestController.class.getName()).log(Level.SEVERE, null, io);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void plot(List<Parameters> list) {
		final CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Time");
		
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Signal Strength");
		
		final LineChart<String, Number> chart = new LineChart<String, Number>(xAxis, yAxis);
		chart.setTitle("Drone Parameter Forensics");
		chart.setPrefWidth(1600);
		chart.setPrefHeight(800);

		XYChart.Series series1 = new XYChart.Series();
		series1.setName("Signal Strength Over Time");
		
		/*XYChart.Series series2 = new XYChart.Series();
		series2.setName("Altitude Graph Over Time");*/
				
		for(Parameters values : list){
			String timeDate = values.getTime_Date();
			String[] split = timeDate.split(" ");
			String time = split[1];
			/*double battery = Double.parseDouble(values.getVoltage());
			double altitude = Double.parseDouble(values.getAltitude());
			double satellites = Double.parseDouble(values.getSatellites_num());
			double tas = Double.parseDouble(values.getTas());
			String v = values.getGps_used();
			int flightModes = Integer.parseInt(values.getF_mode());*/
			int fsk_rssi = Integer.parseInt(values.getFsk_rssi());
			//series1.getData().add(new XYChart.Data(time, fsk_rssi));
			//series2.getData().add(new XYChart.Data(time, altitude));
		}
		
//		Drone Type Name Display
		int DroneType = Integer.parseInt(list.get(4).getVehicle_type());	
		if(DroneType == type1){	
		droneType.setText(DroneTypeName);
		}
		
		final int R = 6371; //radius of earth
		int fN = 0;
		int lN = 0;
		int fLat = 0;
		int lLat = 0;
		int fLong = 0;
		int lLong = 0;
		
		while(fN < list.size() && lN < list.size()){
		double latChange = Double.parseDouble(list.get(fLat).getLatitude()) - Double.parseDouble(list.get(fLat + 1).getLatitude());
		double longChange = Double.parseDouble(list.get(fLong).getLongitude()) - Double.parseDouble(list.get(fLong + 1).getLongitude());
		double latDistance = Math.toRadians(latChange);
		}
		
		
		/*XYChart.Series series2 = new XYChart.Series();
		series2.setName("Altitude Graph Over Time");
		
		for(Parameters values : list){
			String timeDate = values.getTime_Date();
			String[] split = timeDate.split(" ");
			String time = split[1];
			double altitude = Double.parseDouble(values.getAltitude());
			
			series2.getData().add(new XYChart.Data(time, altitude));
		}*/
		
		/*chart.getData().addAll(series1);
		anchorPane.getChildren().addAll(chart);*/
		anchorPane.setPrefWidth(1800);
		anchorPane.setPrefHeight(1000);
	}
}
