package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.core.AuthController;
import application.core.ValidationUtils;
import application.model.Database;

// Class for interacting with the database
class DatabaseHandler {
	public static boolean emailExists(String email) {
		String query = "SELECT 1 FROM users WHERE email = ?";
		try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, email);

			try (ResultSet rs = stmt.executeQuery()) {
				// If a result is found, the email exists
				if (rs.next()) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false; // Email does not exist or an error occurred
	}

	public static boolean saveUser(String name, String email, String password) {
//		System.out.println(name + " " + email + " " + password);
		if (AuthController.signup(name, email, password))
			return true;
		return false;
	}
}

public class SignUpController {

	@FXML
	private TextField nameField;
	@FXML
	private TextField emailField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private PasswordField confirmPasswordField;

	@FXML
	private Label nameError;
	@FXML
	private Label emailError;
	@FXML
	private Label passwordError;
	@FXML
	private Label confirmPasswordError;

	@FXML
	private void handleSignUp() {
		String name = nameField.getText();
		String email = emailField.getText();
		String password = passwordField.getText();
		String confirmPassword = confirmPasswordField.getText();

		// Hide all error labels initially
		nameError.setVisible(false);
		emailError.setVisible(false);
		passwordError.setVisible(false);
		confirmPasswordError.setVisible(false);

		boolean hasError = false;

		// Validate input and show errors
		if (!ValidationUtils.isValidName(name)) {
			nameError.setVisible(true);
			hasError = true;
		}

		if (!ValidationUtils.isValidEmail(email)) {
			emailError.setVisible(true);
			hasError = true;
		}

		if (DatabaseHandler.emailExists(email)) {
			emailError.setVisible(true);
			emailError.setText("Email already exists in the database.");
			hasError = true;
		}

		if (!ValidationUtils.isValidPassword(password)) {
			passwordError.setVisible(true);
			hasError = true;
		}

		if (!ValidationUtils.doPasswordsMatch(password, confirmPassword)) {
			confirmPasswordError.setVisible(true);
			hasError = true;
		}

		// If there are errors, return early
		if (hasError) {
//			Empty all the fields
			nameField.setText("");
			emailField.setText("");
			passwordField.setText("");
			confirmPasswordField.setText("");
			return;
		}

		// Save user to the database if no errors
		if (DatabaseHandler.saveUser(name, email, password)) {
			// if the user is registered redirect him to the login page to signin to his account
			this.switchToLogin();
		} else {
			
			confirmPasswordError.setText("Failed to rigister user!");
			confirmPasswordError.setVisible(true);
			return;
		}
	}

	@FXML
	private void switchToLogin() {
		BaseController currentController = (BaseController) emailField.getScene().getUserData();
		if (currentController != null) {
			currentController.navigateLogin();
		}
	}
}
