package application;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import com.opencsv.CSVReader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;

public class RollPitchYawController {
	FileChooser fileChooser = new FileChooser();
	String filePath;
	// String fileName;
	CSVReader reader = null;

	@FXML
	AnchorPane rootPane;
	
	@FXML
	Group mainGroup;

	@FXML
	public void openFile(ActionEvent event) {

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

	public void readRPY(File file) {
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
			}
			System.out.println("File read!!");

			calcRPY(paramValues);
		} catch (IOException io) {
			Logger.getLogger(UploadController.class.getName()).log(Level.SEVERE, null, io);
		}
	}

	private void calcRPY(List<Parameters> calcandwriteRPY) {
		// 3D object import
		TdsModelImporter model = new TdsModelImporter();
		try {
			URL modelUrl = this.getClass().getResource("hst.3ds");
			model.read(modelUrl);
		} catch (ImportException e) {
			System.out.println("Error importing obj model: " + e.getMessage());
			return;
		}
		final Node[] modelMesh = model.getImport();
		model.close();
		Group group = new Group(modelMesh);
		
		// adding the point light
		PointLight pointLight = new PointLight();
		pointLight = new PointLight(Color.ALICEBLUE);
		pointLight.setTranslateX(800);
		pointLight.setTranslateY(-800);
		pointLight.setTranslateZ(-1000);
		//group1.getChildren().addAll(group, pointLight);
		
		// adding to group
		Group group1 = new Group();
		group1.getChildren().addAll(group, pointLight);

		// adding camera
		Rotate cameraXRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
		Rotate cameraYRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
		Translate cameraPosition = new Translate(-300, -550, -700);
		//Timeline timeline = null;
		PerspectiveCamera camera = new PerspectiveCamera(true);
		camera.getTransforms().addAll(cameraXRotate, cameraYRotate, cameraPosition);
		
		//Adding SubScene
		SubScene subScene = new SubScene(group1, 1000, 400, true, SceneAntialiasing.BALANCED);
		subScene.setCamera(camera);

		// adding to the AnchorPane
		this.rootPane.getChildren().add(subScene);
		// subScene.setRoot(root);

		// getting the Y P R values
		for (Parameters listValues : calcandwriteRPY) {
			double rollValue = Double.parseDouble(listValues.getRoll());
			double pitchValue = Double.parseDouble(listValues.getPitch());
			double yawValue = Double.parseDouble(listValues.getYaw());
			rotateCalc(group, rollValue, pitchValue, yawValue);
		}
	}

	private void rotateCalc(Node n, double roll, double pitch, double yaw) {
		double A11 = Math.cos(roll) * Math.cos(yaw);
		double A12 = Math.cos(pitch) * Math.sin(roll) + Math.cos(roll) * Math.sin(pitch) * Math.sin(yaw);
		double A13 = Math.sin(roll) * Math.sin(pitch) - Math.cos(roll) * Math.cos(pitch) * Math.sin(yaw);
		double A21 = -(Math.cos(yaw) * Math.sin(roll));
		double A22 = Math.cos(roll) * Math.cos(pitch) - Math.sin(roll) * Math.sin(pitch) * Math.sin(yaw);
		double A23 = Math.cos(roll) * Math.sin(pitch) + Math.cos(pitch) * Math.sin(roll) * Math.sin(yaw);
		double A31 = Math.sin(yaw);
		double A32 = -(Math.cos(yaw) * Math.sin(pitch));
		double A33 = Math.cos(pitch) * Math.cos(yaw);

		double angle = Math.acos((A11 + A22 + A33 - 1d) / 2d);
		if (angle != 0d) {
			double denom = 2d * Math.sin(angle);
			Point3D p = new Point3D((A32 - A23) / denom, (A13 - A31) / denom, (A21 - A12) / denom);
			n.setRotationAxis(p);
			n.setRotate(Math.toDegrees(angle));
		}
	}
}
