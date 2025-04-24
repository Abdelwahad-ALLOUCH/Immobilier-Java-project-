module immobilier {
	requires javafx.controls; // For JavaFX UI components
	requires javafx.fxml; // For FXML
	requires java.sql; // For JDBC database connections
	requires javafx.graphics;
	requires com.jfoenix;

	// For graphical elements
//    opens application.view to javafx.fxml;  // Opens the view package for FXML loader
	exports application.controller; // Export controller package
	exports application.model;
	exports application.core;

	opens application.controller to javafx.fxml;

	exports application;

	opens application to javafx.graphics, javafx.fxml;

}
