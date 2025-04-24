package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ContactUsController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField subjectField;

    @FXML
    private TextArea messageArea;

    @FXML
    private Button submitButton;

    @FXML
    public void initialize() {
        submitButton.setOnAction(event -> handleSubmit());
    }

    private void handleSubmit() {
        String name = nameField.getText();
        String email = emailField.getText();
        String subject = subjectField.getText();
        String message = messageArea.getText();

        if (name.isEmpty() || email.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        // Simulate sending the message (e.g., send to a server or save to a database)
        System.out.println("Message sent:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);

        showInfo("Your message has been sent successfully!");
        clearFields();
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        subjectField.clear();
        messageArea.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
