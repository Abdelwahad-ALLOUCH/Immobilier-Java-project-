package application.controller;

import application.core.AuthController;
import application.core.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import application.model.UserSession;

public class LoginController {
	private BaseController baseController;
	@FXML
	private TextField emailField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Label emailError;

	@FXML
	private Label passwordError;

	/* for handling cases where user is login or not */
	@FXML
	public void setBaseController(BaseController baseController) {
		this.baseController = baseController;
	}

	public void handleLogin() {
		String email = emailField.getText();
		String password = passwordField.getText();

		// Hide all error labels initially
		emailError.setVisible(false);
		passwordError.setVisible(false);

		boolean hasError = false;
		if (!ValidationUtils.isValidEmail(email)) {
			emailError.setVisible(true);
			emailError.setText("Invalid email format.");
			hasError = true;
		}
		// Validate email
		if (ValidationUtils.isEmailEmpty(email)) {
			emailError.setVisible(true);
			emailError.setText("Email cannot be empty.");
			hasError = true;
		}
		// Validate password

		if (!ValidationUtils.isValidPassword(password)) {
			passwordError.setVisible(true);
			passwordError.setText("Password must be at least 8 characters.");
			hasError = true;
		}
		if (ValidationUtils.isPasswordEmpty(password)) {
			passwordError.setVisible(true);
			passwordError.setText("Password cannot be empty.");
			hasError = true;
		}

		if (hasError) {
			return;
		}
//		Log in 
		if (AuthController.login(email, password)) {
			// Update button visibility using the BaseController instance
			if (baseController != null) {

				baseController.updateButtonVisibility();
				baseController.navigateHome();
			}
		} else {
			passwordError.setText("Incorrect credentials!");
			passwordError.setVisible(true);
			return;
		}

	}

	@FXML
	private void switchToSignUp() {
		// Retrieve the current controller instance
		BaseController currentController = (BaseController) emailField.getScene().getUserData();
		if (currentController != null) {
			currentController.navigateSignUp();
		}
	}
}
