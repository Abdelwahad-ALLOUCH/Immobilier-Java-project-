package application.controller;

import application.core.ImageStorageUtil;
import application.core.OfferService;
import application.core.OfferValidation;
import application.core.OfferValidation.ValidationResult;
import application.model.Offer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublishOfferController {
    @FXML private TextArea descriptionArea;
    @FXML private TextField priceField;
    @FXML private TextField roomsField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private ComboBox<String> cityComboBox;
    @FXML private TextField cityField;
    @FXML private TextField streetField;
    @FXML private TextField numberField;
    @FXML private Button selectImagesButton;
    @FXML private HBox imageDisplayBox;
    @FXML private Button publishButton;

    // Error labels
    @FXML private Label descriptionError;
    @FXML private Label priceError;
    @FXML private Label roomsError;
    @FXML private Label typeError;
    @FXML private Label cityError;
    @FXML private Label streetError;
    @FXML private Label numberError;
    @FXML private Label imagesError;

    private Map<Control, Label> validationMap = new HashMap<>();
    private List<File> selectedImages = new ArrayList<>();
    private static final int MAX_IMAGES = 7;
    private static final double THUMBNAIL_SIZE = 100; // Size for image thumbnails

    @FXML
    public void initialize() {
        // Initialize type combo box
        typeComboBox.getItems().addAll("Appartment", "House", "Studio", "Villa");
        // Initialize cities
        cityComboBox.setItems(FXCollections.observableArrayList(
            "Casablanca", "Rabat", "Marrakech", "Fes", "Tangier",
            "Agadir", "Meknes", "Oujda", "Kenitra", "Tetouan"
        ));
        
        // Setup validation mapping
        setupValidationMap();
        
        // Add listeners for real-time validation
        addValidationListeners();
        
        // Setup image selection button
        selectImagesButton.setOnAction(e -> handleImageSelection());
        
        // Style the image display box
        imageDisplayBox.setSpacing(10);
        imageDisplayBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5;");
    }

    private void handleImageSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Images");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Get the window from any FXML control
        Stage stage = (Stage) selectImagesButton.getScene().getWindow();
        List<File> newImages = fileChooser.showOpenMultipleDialog(stage);

        if (newImages != null) {
            // Check if adding new images would exceed the limit
            if (selectedImages.size() + newImages.size() > MAX_IMAGES) {
                imagesError.setText("Maximum " + MAX_IMAGES + " images allowed");
                return;
            }

            // Add new images
            for (File file : newImages) {
                if (!selectedImages.contains(file)) {
                    selectedImages.add(file);
                }
            }

            updateImageDisplay();
        }
    }

    private void updateImageDisplay() {
        imageDisplayBox.getChildren().clear();
        imagesError.setText("");

        for (File file : selectedImages) {
            try {
                // Create image and imageview
                Image image = new Image(file.toURI().toString());
                ImageView imageView = new ImageView(image);
                
                // Set size for thumbnail
                imageView.setFitWidth(THUMBNAIL_SIZE);
                imageView.setFitHeight(THUMBNAIL_SIZE);
                imageView.setPreserveRatio(true);

                // Create delete button
                Button deleteButton = new Button("X");
                deleteButton.setOnAction(e -> removeImage(file));
                
                // Create a container for image and delete button
                VBox imageContainer = new VBox();
                imageContainer.setStyle("-fx-border-color: #ddd; -fx-padding: 5; -fx-alignment: center;");
                imageContainer.getChildren().addAll(imageView, deleteButton);
                
                imageDisplayBox.getChildren().add(imageContainer);
            } catch (Exception e) {
                imagesError.setText("Error loading image: " + file.getName());
            }
        }

        // Update the select button text to show remaining slots
        int remainingSlots = MAX_IMAGES - selectedImages.size();
        selectImagesButton.setText("Select Images (" + remainingSlots + " remaining)");
    }

    private void removeImage(File file) {
        selectedImages.remove(file);
        updateImageDisplay();
    }

    private void setupValidationMap() {
        validationMap.put(descriptionArea, descriptionError);
        validationMap.put(priceField, priceError);
        validationMap.put(roomsField, roomsError);
        validationMap.put(typeComboBox, typeError);
        validationMap.put(cityComboBox, cityError);
        validationMap.put(numberField, numberError);
    }

    private void addValidationListeners() {
        // Add change listeners for real-time validation
        descriptionArea.textProperty().addListener((obs, old, newValue) -> validateDescription());
        priceField.textProperty().addListener((obs, old, newValue) -> validatePrice());
        roomsField.textProperty().addListener((obs, old, newValue) -> validateRooms());
        typeComboBox.valueProperty().addListener((obs, old, newValue) -> validateType());
        cityComboBox.valueProperty().addListener((obs, old, newValue) -> validateCity());
        numberField.textProperty().addListener((obs, old, newValue) -> validateNumber());
    }

    private void validateDescription() {
        ValidationResult result = OfferValidation.validateDescription(descriptionArea.getText());
        descriptionError.setText(result.getMessage());
    }

    private void validatePrice() {
        ValidationResult result = OfferValidation.validatePrice(priceField.getText());
        priceError.setText(result.getMessage());
    }

    private void validateRooms() {
        ValidationResult result = OfferValidation.validateRooms(roomsField.getText());
        roomsError.setText(result.getMessage());
    }

    private void validateType() {
        ValidationResult result = OfferValidation.validateType(typeComboBox.getValue());
        typeError.setText(result.getMessage());
    }

    private void validateCity() {
        ValidationResult result = OfferValidation.validateCity(cityComboBox.getValue());
        cityError.setText(result.getMessage());
    }

    private void validateNumber() {
        ValidationResult result = OfferValidation.validateNumber(numberField.getText());
        numberError.setText(result.getMessage());
    }

    private void validateImages() {
        ValidationResult result = OfferValidation.validateImages(selectedImages);
        imagesError.setText(result.getMessage());
    }

    private boolean validateAll() {
        boolean isValid = true;
        
        ValidationResult descResult = OfferValidation.validateDescription(descriptionArea.getText());
        descriptionError.setText(descResult.getMessage());
        isValid &= descResult.isValid();

        ValidationResult priceResult = OfferValidation.validatePrice(priceField.getText());
        priceError.setText(priceResult.getMessage());
        isValid &= priceResult.isValid();

        ValidationResult roomsResult = OfferValidation.validateRooms(roomsField.getText());
        roomsError.setText(roomsResult.getMessage());
        isValid &= roomsResult.isValid();

        ValidationResult typeResult = OfferValidation.validateType(typeComboBox.getValue());
        typeError.setText(typeResult.getMessage());
        isValid &= typeResult.isValid();

        ValidationResult cityResult = OfferValidation.validateCity(cityComboBox.getValue());
        cityError.setText(cityResult.getMessage());
        isValid &= cityResult.isValid();

        ValidationResult numberResult = OfferValidation.validateNumber(numberField.getText());
        numberError.setText(numberResult.getMessage());
        isValid &= numberResult.isValid();

        ValidationResult imagesResult = OfferValidation.validateImages(selectedImages);
        imagesError.setText(imagesResult.getMessage());
        isValid &= imagesResult.isValid();

        return isValid;
    }
    @FXML
    private void handlePublish() {
        boolean isValid = validateAll();
        if (isValid) {
            // Proceed with publishing
            publishOffer();
        }
    }

    // publish offer image
    private void publishOffer() {
    try {
        // Create instance of OfferService
        OfferService offerService = new OfferService();
        
        // Gather all the data from form fields
        String description = descriptionArea.getText();
        Integer nbrChambre = Integer.parseInt(roomsField.getText());
        String numero = numberField.getText();
        Double price = Double.parseDouble(priceField.getText());
        String street = streetField.getText();
        String type = typeComboBox.getValue();
        String ville = cityComboBox.getValue();
        
        // Publish the offer
        offerService.publishOffer(
            description,
            nbrChambre,
            numero,
            price,
            street,
            type,
            ville,
            selectedImages
        );
        
        // Show success message
        showAlert(Alert.AlertType.INFORMATION, "Success", "Offer published successfully!");
        
        // Clear the form
        clearForm();
        
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Database Error", 
                 "Failed to publish offer: " + e.getMessage());
    } catch (IOException e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "File Error", 
                 "Failed to save images: " + e.getMessage());
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Input Error", 
                 "Please ensure all numeric fields are filled correctly");
    } catch (Exception e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Error", 
                 "An unexpected error occurred: " + e.getMessage());
    }
}private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearForm() {
        descriptionArea.clear();
        priceField.clear();
        roomsField.clear();
        typeComboBox.setValue(null);
        cityComboBox.setValue(null);
        streetField.clear();
        numberField.clear();
        selectedImages.clear();
        updateImageDisplay();
    }
}