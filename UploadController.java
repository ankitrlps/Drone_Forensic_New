package application;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

public class UploadController {

	@FXML
	WebView webView;
	@FXML
	Text distanceTravelled, flightDate, flightLength, maxAlt;
	@FXML
	Tab overviewTab;
	@FXML
	Text fileReadNotice;

	FileChooser fileChooser = new FileChooser();
	String filePath;
	String fileName;
	CSVReader reader = null;
	CSVWriter writer = null;
	// String csvWriteFile =
	// "C://Users//ankit//Documents//testCSVWithoutHeader.csv";
	FileWriter kmlWriter;
	DocumentBuilderFactory docFactory;
	DocumentBuilder docbuilder;
	TransformerFactory transformFac;
	Transformer transform;
	DOMSource source;
	StreamResult result;
	Date dateStart = null;
	Date dateEnd = null;
	private static final double radius = 6371d;

	List<Parameters> paramValues = new ArrayList<Parameters>();

	@FXML
	public void uploadFile(ActionEvent event) {
		fileChooser.setTitle("choose a File");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			filePath = file.getAbsolutePath();
			fileName = file.getName();
			// readFile(file);
			try {
				reader = new CSVReader(new FileReader(filePath), ',', '\'', 2);
				String[] column = null;
				while ((column = reader.readNext()) != null) {
					Parameters params = new Parameters(column[0], column[1], column[2], column[3], column[4], column[5],
							column[6], column[7], column[8], column[9], column[10], column[11], column[12], column[13],
							column[14], column[15], column[16], column[17], column[18], column[19], column[20],
							column[21]);
					paramValues.add(params);
				}
				fileReadNotice.setText("File Read Success");

			} catch (IOException io) {
				Logger.getLogger(UploadController.class.getName()).log(Level.SEVERE, null, io);
			}
		} else {
			fileReadNotice.setText("File Read Failed");
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setTitle("Error");
			errorAlert.setHeaderText("Error Information");
			errorAlert.setContentText("Error while selection");
			errorAlert.show();
		}
	}

	@FXML
	private void distance(ActionEvent event) {
		DecimalFormat df = new DecimalFormat("####.##");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		double latChange;
		double longChange;
		double distanceOfTwoValues, a, c;
		double totalDistance = 0;
		int latIncrement = 0;
		int longIncrement = 0;
		double deg2Rad = Math.PI / 180;
		double altitude = Double.parseDouble(paramValues.get(0).getAltitude());

		for (int i = 0; i < paramValues.size() - 1; i++) {
			double lat1 = Double.parseDouble(paramValues.get(latIncrement).getLatitude());
			double lat2 = Double.parseDouble(paramValues.get(++latIncrement).getLatitude());

			double long1 = Double.parseDouble(paramValues.get(longIncrement).getLongitude());
			double long2 = Double.parseDouble(paramValues.get(++longIncrement).getLongitude());

			latChange = (lat1 - lat2) * deg2Rad;
			longChange = (long1 - long2) * deg2Rad;

			a = Math.sin(latChange / 2) * Math.sin(latChange / 2) + Math.cos(deg2Rad * (lat1))
					* Math.cos(deg2Rad * (lat2)) * Math.sin(longChange / 2) * Math.sin(longChange / 2);

			c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

			distanceOfTwoValues = radius * c * 1000;

			distanceOfTwoValues = Math.sqrt(Math.pow(distanceOfTwoValues, 2));

			totalDistance = totalDistance + distanceOfTwoValues;

			if (altitude < Double.parseDouble(paramValues.get(i).getAltitude())) {
				altitude = Double.parseDouble(paramValues.get(i).getAltitude());
			}
		}
		maxAlt.setText(altitude + " meters");

		distanceTravelled.setText(df.format(totalDistance) + " meters");

		// Date and Time

		String startTimeFromLog = paramValues.get(0).getTime_Date();
		String splitStartTimeDate[] = startTimeFromLog.split(" ");

		String endTimeFromLog = paramValues.get(paramValues.size() - 1).getTime_Date();
		String splitEndTimeDate[] = endTimeFromLog.split(" ");

		// Date
		String unformattedDate = splitStartTimeDate[0];
		String date = unformattedDate.substring(0, 4) + "-" + unformattedDate.substring(4, 6) + "-"
				+ unformattedDate.substring(6, 8);
		flightDate.setText(date);

		// format time
		String unformattedStartTime = splitStartTimeDate[1].substring(0, 8);
		String unformattedEndTime = splitEndTimeDate[1].substring(0, 8);

		try {
			dateStart = simpleDateFormat.parse(unformattedStartTime);
			dateEnd = simpleDateFormat.parse(unformattedEndTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Final time difference
		long timeDiff = dateEnd.getTime() - dateStart.getTime();

		long timeMinutes = timeDiff / (60 * 1000) % 60;
		long timeSeconds = timeDiff / 1000 % 60;

		flightLength.setText(timeMinutes + " mins" + " " + timeSeconds + " secs");

	}

	@FXML
	public void openMaps(ActionEvent event) {
		WebEngine engine = webView.getEngine();
		URL localURL = getClass().getResource("kmlTest.html");
		// String url = "http://eclipse.org";
		engine.load(localURL.toExternalForm());

	}

	@FXML
	public void convertCSVToKML(ActionEvent event) {
		try {

			/*
			 * date_Time = paramValues.get(0).getTime_Date();
			 * System.out.println(date_Time);
			 */

			/*
			 * Parameters firstElement = null; if (paramValues.size() > 0) {
			 * firstElement = paramValues.get(1); }
			 * System.out.println(firstElement);
			 */
			// System.out.println(date_tim);

			/*
			 * String[] date = new String[]{paramValues.get(0).getTime_Date(),
			 * paramValues.get(0).getAltitude(),
			 * paramValues.get(0).getLatitude(),
			 * paramValues.get(0).getLongitude()}; System.out.println(date);
			 */

			/*
			 * String[] retrieveElements = null; for (int i = 0; i <
			 * paramValues.size(); i++) { retrieveElements = new String[] {
			 * paramValues.get(i).getTime_Date(),
			 * paramValues.get(i).getAltitude(),
			 * paramValues.get(i).getLatitude(),
			 * paramValues.get(i).getLongitude() }; }
			 */
			String firstLineTimeElement = null;
			String firstLineLatitude = null;
			String firstLineLongitude = null;
			String firstLineAltitudeElement = null;
			String secondLineAltitudeElement = null;
			String lastLineTimeElement = null;
			String lastLineAltitudeElement = null;
			String lastLineLatitudeElement = null;
			String lastLineLongitudeElement = null;
			String testLastPitch;

			if (paramValues != null && !paramValues.isEmpty()) {
				firstLineTimeElement = paramValues.get(0).getTime_Date();
				firstLineLatitude = paramValues.get(0).getLatitude();
				firstLineLongitude = paramValues.get(0).getLongitude();
				firstLineAltitudeElement = paramValues.get(0).getAltitude();
				secondLineAltitudeElement = paramValues.get(10).getAltitude();
				lastLineTimeElement = paramValues.get(paramValues.size() - 1).getTime_Date();
				lastLineAltitudeElement = paramValues.get(paramValues.size() - 1).getAltitude();
				lastLineLatitudeElement = paramValues.get(paramValues.size() - 1).getLatitude();
				lastLineLongitudeElement = paramValues.get(paramValues.size() - 1).getLongitude();
				testLastPitch = paramValues.get(paramValues.size() - 1).getPitch();
				System.out.println("Here it is: " + lastLineAltitudeElement + "  " + lastLineLatitudeElement + "  "
						+ lastLineLongitudeElement + "   " + testLastPitch);
			}

			System.out.println(
					firstLineTimeElement + " | " + firstLineAltitudeElement + " | " + secondLineAltitudeElement);

			/*
			 * String[] dateTimeAllRows = null; for (int i = 0; i <
			 * paramValues.size(); i++) {
			 * 
			 * dateTimeAllRows = new String[] {
			 * paramValues.get(i).getTime_Date() }; }
			 */

			String[] firstDateTimeDivide = firstLineTimeElement.split(" ");
			String firstDate = firstDateTimeDivide[0];
			String firstTime = firstDateTimeDivide[1];
			System.out.println(firstDate);
			System.out.println(firstTime);
			String[] lastDateTimeDivide = lastLineTimeElement.split(" ");
			String lastDate = lastDateTimeDivide[0];
			String lastTime = lastDateTimeDivide[1];

			docFactory = DocumentBuilderFactory.newInstance();
			docbuilder = docFactory.newDocumentBuilder();
			Document doc = docbuilder.newDocument();
			Element rootElement = doc.createElement("kml");
			doc.appendChild(rootElement);

			Attr attribute1 = doc.createAttribute("xmlns");
			attribute1.setValue("http://www.opengis.net/kml/2.2");
			rootElement.setAttributeNode(attribute1);

			Attr attribute2 = doc.createAttribute("xmlns:gx");
			attribute2.setValue("http://www.google.com/kml/ext/2.2");
			rootElement.setAttributeNode(attribute2);

			Element document = doc.createElement("Document");
			rootElement.appendChild(document);

			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode("FlightLog evaluation for Yuneec copter"));
			document.appendChild(name);

			Element desc = doc.createElement("description");
			desc.appendChild(doc.createTextNode(firstLineTimeElement + " - " + fileName));
			document.appendChild(desc);

			/* ~~~Style 1~~~ */

			Element style1 = doc.createElement("Style");
			document.appendChild(style1);

			Attr style1Attr = doc.createAttribute("id");
			style1Attr.setValue("Flightpath");
			style1.setAttributeNode(style1Attr);

			Element linestyle1 = doc.createElement("LineStyle");
			style1.appendChild(linestyle1);

			Element color1 = doc.createElement("color");
			color1.appendChild(doc.createTextNode("ff00ff00"));
			linestyle1.appendChild(color1);

			Element width1 = doc.createElement("width");
			width1.appendChild(doc.createTextNode("2"));
			linestyle1.appendChild(width1);

			Element polyStyle = doc.createElement("PolyStyle");
			style1.appendChild(polyStyle);

			Element colorPoly = doc.createElement("color");
			colorPoly.appendChild(doc.createTextNode("7f00ff00"));
			polyStyle.appendChild(colorPoly);

			/* ~~~Style 2~~~ */

			Element style2 = doc.createElement("Style");
			Attr style2Attr = doc.createAttribute("id");
			style2Attr.setValue("GrndStn");
			style2.setAttributeNode(style2Attr);
			document.appendChild(style2);

			Element linestyle2 = doc.createElement("LineStyle");
			style2.appendChild(linestyle2);

			Element color2 = doc.createElement("color");
			color2.appendChild(doc.createTextNode("FF000000"));
			linestyle2.appendChild(color2);

			Element width2 = doc.createElement("width");
			width2.appendChild(doc.createTextNode("2"));
			linestyle2.appendChild(width2);

			/* ~~~Style 3~~~ */

			Element style3 = doc.createElement("Style");
			Attr style3Attr = doc.createAttribute("id");
			style3Attr.setValue("starting");
			style3.setAttributeNode(style3Attr);
			document.appendChild(style3);

			Element iconStyle3 = doc.createElement("IconStyle");
			Element icon3 = doc.createElement("Icon");
			Element href3 = doc.createElement("href");
			href3.appendChild(doc.createTextNode("http://maps.google.com/mapfiles/dir_0.png"));
			icon3.appendChild(href3);
			iconStyle3.appendChild(icon3);
			style3.appendChild(iconStyle3);

			/* ~~~Style 4~~~ */

			Element style4 = doc.createElement("Style");
			Attr style4Attr = doc.createAttribute("id");
			style4Attr.setValue("landing");
			style4.setAttributeNode(style4Attr);
			document.appendChild(style4);

			Element iconStyle4 = doc.createElement("IconStyle");
			Element icon4 = doc.createElement("Icon");
			Element href4 = doc.createElement("href");
			href4.appendChild(doc.createTextNode("http://maps.google.com/mapfiles/dir_walk_60.png"));
			icon4.appendChild(href4);
			iconStyle4.appendChild(icon4);
			style4.appendChild(iconStyle4);

			/* ~~~ Placemark 1 ~~~ */

			Element placemark1 = doc.createElement("Placemark");
			Element timeStamp1 = doc.createElement("TimeStamp");
			Element when1 = doc.createElement("when");
			when1.appendChild(doc.createTextNode(firstDate + "T" + firstTime + "Z"));
			timeStamp1.appendChild(when1);
			placemark1.appendChild(timeStamp1);
			document.appendChild(placemark1);
			Element styleURLStarting = doc.createElement("styleUrl");
			styleURLStarting.appendChild(doc.createTextNode("#starting"));
			placemark1.appendChild(styleURLStarting);
			Element point1 = doc.createElement("Point");
			Element coordinates1 = doc.createElement("coordinates");
			coordinates1.appendChild(doc.createTextNode(firstLineLongitude + "," + firstLineLatitude + "," + "0.0"));
			point1.appendChild(coordinates1);
			placemark1.appendChild(point1);

			/* ~~~ Placemark 2 ~~~ */

			Element placemark2 = doc.createElement("Placemark");
			document.appendChild(placemark2);
			Element name1 = doc.createElement("name");
			name1.appendChild(doc.createTextNode("Yuneec Typhoon H Flight Path" + firstLineTimeElement));
			placemark2.appendChild(name1);
			Element desc1 = doc.createElement("description");
			desc1.appendChild(doc.createTextNode(fileName));
			placemark2.appendChild(desc1);
			Element styleURLFlightPath = doc.createElement("styleUrl");
			styleURLFlightPath.appendChild(doc.createTextNode("#Flightpath"));
			placemark2.appendChild(styleURLFlightPath);
			Element lineString = doc.createElement("LineString");
			placemark2.appendChild(lineString);
			Element altOffset = doc.createElement("gx:altitudeOffset");
			altOffset.appendChild(doc.createTextNode("190.0"));
			lineString.appendChild(altOffset);
			Element altMode = doc.createElement("altitudeMode");
			altMode.appendChild(doc.createTextNode("absolute"));
			lineString.appendChild(altMode);
			Element coordinates2 = doc.createElement("coordinates");
			for (Parameters listValues : paramValues) {

				coordinates2.appendChild(doc.createTextNode("\n" + listValues.getLongitude() + ","
						+ listValues.getLatitude() + "," + listValues.getAltitude() + "\n"));
			}
			lineString.appendChild(coordinates2);

			/* ~~~ Placemark 3 ~~~ */
			lineString.appendChild(coordinates2);
			Element placemark3 = doc.createElement("Placemark");
			document.appendChild(placemark3);
			Element timeStamp2 = doc.createElement("TimeStamp");
			placemark3.appendChild(timeStamp2);
			Element when2 = doc.createElement("when");
			when2.appendChild(doc.createTextNode(lastDate + "T" + lastTime + "Z"));
			timeStamp2.appendChild(when2);
			Element styleURLLanding = doc.createElement("styleUrl");
			styleURLLanding.appendChild(doc.createTextNode("#landing"));
			placemark3.appendChild(styleURLLanding);
			Element point2 = doc.createElement("Point");
			placemark3.appendChild(point2);
			Element coordinates3 = doc.createElement("coordinates");
			coordinates3.appendChild(doc.createTextNode(
					lastLineLongitudeElement + "," + lastLineLatitudeElement + "," + lastLineAltitudeElement));
			point2.appendChild(coordinates3);

			transformFac = TransformerFactory.newInstance();
			try {
				transform = transformFac.newTransformer();
				transform.setOutputProperty(OutputKeys.INDENT, "yes");
				transform.setOutputProperty(OutputKeys.METHOD, "xml");
				transform.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				source = new DOMSource(doc);
				result = new StreamResult(new File("C://program test folder//Kml file.kml"));

				transform.transform(source, result);

				System.out.println("Saved!!");
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}