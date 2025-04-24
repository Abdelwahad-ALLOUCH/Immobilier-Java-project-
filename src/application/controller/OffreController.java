package application.controller;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import application.model.Offer;

import javafx.scene.layout.BorderPane;


public class OffreController {
    private Offer offer;


    @FXML
    private Label priceLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label roomsLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label numberLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private HBox imageContainer;
    
    public void setOffer(Offer offer) {
        this.offer = offer;
        // Your existing code to populate the view with offer details
        displayOfferDetails();
    }

    @FXML
    private void handleBackToOffers() {
        // Get the parent OffersController and call showOffersView
    	BaseController bc = (BaseController) cityLabel.getScene().getUserData();
    	if (bc != null) bc.navigateOffers();

    }
    
    @FXML
    public void initialize() {
        // Initialize any necessary components
    }
    

    
    private void displayOfferDetails() {
        if (offer != null) {
            priceLabel.setText(String.format("%.2f MAD", offer.getPrice()));
            cityLabel.setText(offer.getCity());
            streetLabel.setText(offer.getStreet());
            typeLabel.setText(offer.getType());
            roomsLabel.setText(offer.getRooms() + " Rooms");
            descriptionLabel.setText(offer.getDescription());
            numberLabel.setText("Number: " + offer.getNumber());
            dateLabel.setText("Posted on: " + offer.getCreationDate());
            
            // Display images
            displayImages();
        }
    }
    
    private void displayImages() {
        imageContainer.getChildren().clear();
        
        for (String imagePath : offer.getImagePaths()) {
            try {
                ImageView imageView = new ImageView(new Image("file:" + imagePath));
                imageView.setFitHeight(200);
                imageView.setFitWidth(300);
                imageView.setPreserveRatio(true);
                
                // Add click handler to show full-size image
                imageView.setOnMouseClicked(e -> showFullSizeImage(imagePath));
                
                imageContainer.getChildren().add(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void showFullSizeImage(String imagePath) {
        try {
            Stage imageStage = new Stage();
            ImageView fullImageView = new ImageView(new Image("file:" + imagePath));
            
            // Set reasonable max dimensions
            fullImageView.setFitWidth(800);
            fullImageView.setFitHeight(600);
            fullImageView.setPreserveRatio(true);
            
            ScrollPane scrollPane = new ScrollPane(fullImageView);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            
            imageStage.setScene(new Scene(scrollPane));
            imageStage.setTitle("Full Size Image");
            imageStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}