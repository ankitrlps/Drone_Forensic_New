package battery;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import application.Parameters;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class SeparateLinesController {

	private FileChooser fileChooser = new FileChooser();
	private String filePath;
	private CSVReader reader = null;
	private FileWriter fileWriter = null;
	private CSVWriter writer = null;

	private static final String[] csvHeader = { "LatChange", "LongChange" };

	@FXML
	private void upload() {
		fileChooser.setTitle("choose a File");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			filePath = file.getAbsolutePath();
		} else {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setTitle("Error");
			errorAlert.setHeaderText("Error Information");
			errorAlert.setContentText("Error while selection");
			errorAlert.show();
		}

		try {
			reader = new CSVReader(new FileReader(filePath), ',', '\'', 2);

			String[] column = null;
			// String[] splitDateTime = null;
			List<Parameters> paramValues = new ArrayList<Parameters>();
			while ((column = reader.readNext()) != null) {
				Parameters params = new Parameters(column[0], column[1], column[2], column[3], column[4], column[5],
						column[6], column[7], column[8], column[9], column[10], column[11], column[12], column[13],
						column[14], column[15], column[16], column[17], column[18], column[19], column[20], column[21]);
				paramValues.add(params);
				// reader.close();
			}
			separateLines(paramValues);
		} catch (IOException io) {
			Logger.getLogger(SeparateLinesController.class.getName()).log(Level.SEVERE, null, io);
		}
	}

	/*
	 * list.get(i).getTime_Date() + " | " + list.get(i).getRoll() + " | " +
	 * list.get(i).getPitch() + " | " + list.get(i).getYaw() + " | " +
	 * list.get(i).getLongitude() + " | " + list.get(i).getLatitude();
	 */

	private void separateLines(List<Parameters> list) {
		try {
			fileWriter = new FileWriter("C://program test folder//LatLongChange_New.csv");
			writer = new CSVWriter(fileWriter, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

			writer.writeNext(csvHeader);

			DecimalFormat df = new DecimalFormat("###.#######");
			int fn = 1;
			int ln = 100;
			double firstLat = 0;
			double lastLat = 0;
			double firstLong = 0;
			double lastLong = 0;
			String changeLat;
			String changeLong;

			while (fn < list.size() - 100 && ln < list.size() - 100) {

				firstLat = Double.parseDouble(list.get(fn).getLatitude());
				firstLong = Double.parseDouble(list.get(fn).getLongitude());
				fn = fn + 100;

				lastLat = Double.parseDouble(list.get(ln).getLatitude());
				lastLong = Double.parseDouble(list.get(fn).getLongitude());
				ln = ln + 100;

				changeLat = df.format(firstLat - lastLat);
				changeLong = df.format(firstLong - lastLong);
				System.out.println("Lat: " + changeLat + " | Long: " + changeLong);

				String[] writeChange = { changeLat, changeLong };

				writer.writeNext(writeChange);
			}
			System.out.println("CSV file written successfully");
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				writer.flush();
				reader.close();
				//fileWriter.close();
				writer.close();
			} catch (IOException io) {
				System.out.println("IO ocurred");
				io.printStackTrace();
			}
		}
	}
}

/*
 * String getValues = list.get(i).getTime_Date().substring(9, 21) + " | " +
 * list.get(i).getRoll() + " | " + list.get(i).getPitch() + " | " +
 * list.get(i).getYaw() + " | " + list.get(i).getLongitude() + " | " +
 * list.get(i).getLatitude();
 */

/*
 * if (i == (n * 100 + 1)) { firstLat = i; //
 * Double.parseDouble(list.get(i).getLatitude()); ++n; } if (i % 100 == 0) {
 * lastLat = i; // Double.parseDouble(list.get(i).getLatitude()); } //if
 * (!(firstLat == 0) && !(lastLat == 0)) { changeLat = firstLat - lastLat;
 * System.out.println("FirstLat: " + firstLat + " | LastLat: " + lastLat +
 * " | Change: " + changeLat);
 * 
 * 
 * // i = i + 20;
 */