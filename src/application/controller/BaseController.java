package application.controller;

import application.model.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BaseController {
    @FXML
    private StackPane contentArea;

    @FXML
    private Button signupButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button logoutButton;
  
    @FXML
    private MenuButton publishButton;

    @FXML
    private VBox adminNavBar;

    public void initialize() {
        // Initialize button visibility
        updateButtonVisibility();

        // Load the Home page by default
        navigateHome();
    }

    public void navigateHome() {
        loadContent("Home.fxml");
    }

    public void navigateOffers() {
        loadContent("Offers.fxml");
    }

    public void navigateLogin() {
        loadContent("Login.fxml");
    }

    public void navigateSignUp() {
        loadContent("SignUp.fxml");
    }

    @FXML
    private void navigateAdminDashboard() {
        // Load dashboard view
        loadContent("adminDashboard.fxml");
    }

    @FXML
    private void navigateAdminUsers() {
        // Load users view
        loadContent("adminUsers.fxml");
    }

    @FXML
    private void navigateAdminOffers() {
        // Load offers view
        loadContent("adminOffers.fxml");
    }

    @FXML
    private void navigateAdminExpectations() {
        // Load expectations view
        loadContent("adminExpectations.fxml");
    }

    @FXML
    private void handleLogout() {
        // Clear the user session and update button visibility
        UserSession.getInstance().clearSession();
        updateButtonVisibility();
        navigateHome();
    }

    public void navigatePublishExpectation() {
        this.loadContent("PublishExpectation.fxml");
    }

    public void navigatePublishOffer() {
        this.loadContent("PublishOffer.fxml");
    }

    public void navigateContact() {
        // Ensure the Contact Us page is loaded when this method is called
        this.loadContent("Contact.fxml");
    }

    private void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/" + fxmlFile));
            Node content = loader.load();

            // Get the controller for the loaded FXML
            Object controller = loader.getController();
            if (controller instanceof LoginController) {
                // Pass the BaseController instance to LoginController
                ((LoginController) controller).setBaseController(this);
            }

            // Set the loaded content to the StackPane
            contentArea.getChildren().setAll(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateButtonVisibility() {
        boolean isLoggedIn = UserSession.getInstance().getUserId() != 0;
        boolean isAdmin = UserSession.getInstance().getIsAdmin();

        loginButton.setVisible(!isLoggedIn);
        loginButton.setManaged(!isLoggedIn);
        signupButton.setVisible(!isLoggedIn);
        signupButton.setManaged(!isLoggedIn);
        logoutButton.setVisible(isLoggedIn);
        logoutButton.setManaged(isLoggedIn);

        publishButton.setVisible(isLoggedIn);
        publishButton.setManaged(isLoggedIn);

        adminNavBar.setVisible(isLoggedIn && isAdmin);
        adminNavBar.setManaged(isLoggedIn && isAdmin);
    }
}
