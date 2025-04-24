package application.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Router {

    public static void to(Stage stage, String fxmlFile, String cssFile) {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(Router.class.getResource("/application/view/" + fxmlFile));

            // Create and apply the scene
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Router.class.getResource("/css/" + cssFile).toExternalForm());
            stage.setScene(scene);

            // Ensure stage is maximized and centered
            stage.setMaximized(true);  // Reapply maximization
            stage.centerOnScreen();   // Reapply centering
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
