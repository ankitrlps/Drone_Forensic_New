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

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class TestController {

	@FXML
	AnchorPane anchorPane;

	@FXML
	Button startButton;
	// Group modelGroup;
	FileChooser fileChooser = new FileChooser();
	String filePath;
	CSVReader reader = null;
	TdsModelImporter model = new TdsModelImporter();
	// Text text = new Text();
	// ArrayList<Double> an = new ArrayList<>();
	// Group modelGroup;

	private final Rotate cameraXRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
	private final Rotate cameraYRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
	private final Translate cameraPosition = new Translate(-300, -550, -700);
	private double dragStartX, dragStartY, dragStartRotateX, dragStartRotateY;

	@FXML
	private void upload(ActionEvent event) {

		fileChooser.setTitle("choose a File");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			filePath = file.getAbsolutePath();
			try {
				reader = new CSVReader(new FileReader(filePath), ',', '\'', 2);

				String[] column = null;
				// String[] splitDateTime = null;
				List<Parameters> paramValues = new ArrayList<Parameters>();
				while ((column = reader.readNext()) != null) {
					Parameters params = new Parameters(column[0], column[1], column[2], column[3], column[4], column[5],
							column[6], column[7], column[8], column[9], column[10], column[11], column[12], column[13],
							column[14], column[15], column[16], column[17], column[18], column[19], column[20],
							column[21]);

					paramValues.add(params);
				}
				showFigure(paramValues);
			} catch (IOException io) {
				Logger.getLogger(TestController.class.getName()).log(Level.SEVERE, null, io);
			}
		} else {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setTitle("Error");
			errorAlert.setHeaderText("Error Information");
			errorAlert.setContentText("Error while selection");
			errorAlert.show();
		}

	}

	private Group show3D() {
		try {
			URL modelUrl = this.getClass().getResource("hst.3ds");
			model.read(modelUrl);
		} catch (ImportException e) {
			System.out.println("Error importing obj model: " + e.getMessage());
		}
		final Node[] modelMesh = model.getImport();
		model.close();
		Group group = new Group();
		group.getChildren().addAll(modelMesh);

		return group;
	}

	private Group buildScene(Group modelGroup) {
		Group group1 = new Group();
		PointLight pointLight = new PointLight();
		pointLight = new PointLight(Color.ALICEBLUE);
		pointLight.setTranslateX(800);
		pointLight.setTranslateY(-900);
		pointLight.setTranslateZ(-1000);

		group1.getChildren().addAll(modelGroup, pointLight);
		return group1;
	}

	private SubScene createSubScene(Group groupScene) {
		PerspectiveCamera camera = new PerspectiveCamera();
		camera.setTranslateX(-300);
		camera.setTranslateY(0);
		camera.setTranslateZ(200);
		camera.getTransforms().addAll(cameraXRotate, cameraYRotate, cameraPosition);
		SubScene scene = new SubScene(groupScene, 300, 200, true, SceneAntialiasing.BALANCED);
		scene.widthProperty().bind((this.anchorPane).widthProperty());
		scene.heightProperty().bind((this.anchorPane).heightProperty());
		scene.setCamera(camera);

		scene.addEventHandler(MouseEvent.ANY, (MouseEvent event) -> {
			if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
				dragStartX = event.getSceneX();
				dragStartY = event.getSceneY();
				dragStartRotateX = cameraXRotate.getAngle();
				dragStartRotateY = cameraYRotate.getAngle();
			} else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				double xDelta = event.getSceneX() - dragStartX;
				double yDelta = event.getSceneY() - dragStartY;
				cameraXRotate.setAngle(dragStartRotateX - (yDelta * 0.7));
				cameraYRotate.setAngle(dragStartRotateY + (xDelta * 0.7));
			}
		});

		return scene;
	}

	private void showFigure(List<Parameters> list) {
		Group modelGroup = show3D();
		Group buildModelGroup = buildScene(modelGroup);
		SubScene mainSubScene = createSubScene(buildModelGroup);
		double i = 600d;
		Timeline timeline = new Timeline();
		Node n = modelGroup;

		for (Parameters listValues : list) {
			double rollValue = Double.parseDouble(listValues.getRoll());
			double pitchValue = Double.parseDouble(listValues.getPitch());
			double yawValue = Double.parseDouble(listValues.getYaw());
			// calc(modelGroup, rollValue, pitchValue,
			// yawValue);

			double A11 = Math.cos(rollValue) * Math.cos(yawValue);
			double A12 = Math.cos(pitchValue) * Math.sin(rollValue)
					+ Math.cos(rollValue) * Math.sin(pitchValue) * Math.sin(yawValue);
			double A13 = Math.sin(rollValue) * Math.sin(pitchValue)
					- Math.cos(rollValue) * Math.cos(pitchValue) * Math.sin(yawValue);
			double A21 = -(Math.cos(yawValue) * Math.sin(rollValue));
			double A22 = Math.cos(rollValue) * Math.cos(pitchValue)
					- Math.sin(rollValue) * Math.sin(pitchValue) * Math.sin(yawValue);
			double A23 = Math.cos(rollValue) * Math.sin(pitchValue)
					+ Math.cos(pitchValue) * Math.sin(rollValue) * Math.sin(yawValue);
			double A31 = Math.sin(yawValue);
			double A32 = -(Math.cos(yawValue) * Math.sin(pitchValue));
			double A33 = Math.cos(pitchValue) * Math.cos(yawValue);
System.out.println(rollValue + "" + pitchValue + "" + yawValue);
			double angle = Math.acos((A11 + A22 + A33 - 1d) / 2d);
			// an.add(angle);
			if (angle != 0d) {
				double denom = 2d * Math.sin(angle);
				Point3D p = new Point3D((A32 - A23) / denom, (A13 - A31) / denom, (A21 - A12) / denom);
				n.setRotationAxis(p);
				n.setRotate(Math.toDegrees(angle));
				timeline.getKeyFrames()
						.addAll(new KeyFrame(Duration.millis(i),
								new KeyValue(n.rotateProperty(), n.getRotate(), Interpolator.LINEAR)),
								new KeyFrame(Duration.seconds(i++/100), new KeyValue(n.rotateProperty(),
										n.getRotate() + Math.toDegrees(angle), Interpolator.LINEAR)),
								new KeyFrame(Duration.millis(500)));
			}

		}
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				timeline.play();	
			}
		});

		this.anchorPane.getChildren().addAll(mainSubScene);

	}

}