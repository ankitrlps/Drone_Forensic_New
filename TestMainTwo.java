package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestMainTwo extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			//BorderPane pane = (BorderPane)FXMLLoader.load(getClass().getResource("Test.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Test.fxml"));
			Parent root = loader.load();
			primaryStage.setTitle("Digital Drone Forensics");
			Scene scene = new Scene(root, 1000, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			//primaryStage.setMaximized(true);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
