package application.controller;

import application.model.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SignInUpController {
	@FXML
	private Button loginButton;

	@FXML
	private Button signupButton;

	@FXML
	private Button logoutButton;

	public SignInUpController(Button loginButton, Button signupButton, Button logoutButton) {
		this.loginButton = loginButton;
		this.signupButton = signupButton;
		this.logoutButton = logoutButton;

	}

	@FXML
	private void handleLogin() {
		// Simulate login success and store user data
		updateButtonVisibility();
	}

	@FXML
	private void handleLogout() {
		// Clear user session
		UserSession.getInstance().clearSession();
		updateButtonVisibility();
	}

	private void updateButtonVisibility() {
		boolean isLoggedIn = UserSession.getInstance().getUserId() != 0;
		loginButton.setVisible(!isLoggedIn);
		signupButton.setVisible(!isLoggedIn);
		logoutButton.setVisible(isLoggedIn);
	}
}
