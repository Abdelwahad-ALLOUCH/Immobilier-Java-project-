package application;

import java.sql.Connection;

import application.controller.BaseController;
import application.model.Database;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Connection c = Database.getConnection();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/Layout.fxml"));
		Parent root = loader.load();

		// Set the controller as user data on the scene
		BaseController controller = loader.getController();
		// Create the scene
		Scene scene = new Scene(root);

		// this is important when switching from login to signup.
		scene.setUserData(controller);
		//
		scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/css/layout.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/css/publish.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/css/home.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/css/offer.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/css/offers.css").toExternalForm());

		// Set the stage with the scene
		primaryStage.setScene(scene);
		primaryStage.setTitle("Immobilier");

		// Maximize the window on startup
		primaryStage.setMaximized(true);
//		primaryStage.setWidth(1020);
//		primaryStage.setHeight(729);
		// Center the stage on the screen
		primaryStage.centerOnScreen();

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void stop() throws Exception {
		Database.closeConnection();
		System.out.println("Application stopped.");
		super.stop();
	}
}
