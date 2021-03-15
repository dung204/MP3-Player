package com.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setTitle("MP3 Player");
		primaryStage.setResizable(false);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Frame.fxml"));
		Parent root = loader.load();

		primaryStage.setOnCloseRequest(windowEvent -> {
			Platform.exit();
			System.exit(0);
		});

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

		Controller controller = loader.getController();
		scene.setOnScroll(scrollEvent -> {
			if(scrollEvent.getDeltaY() < 0) {
				controller.decreaseVolumeByScrolling();
			}
			if(scrollEvent.getDeltaY() > 0) {
				controller.increaseVolumeByScrolling();
			}
		});
	}
}
