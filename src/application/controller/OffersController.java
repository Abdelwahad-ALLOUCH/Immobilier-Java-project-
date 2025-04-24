package application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import application.core.OfferService;
import application.model.Offer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class OffersController {
	@FXML
	private TextField searchField;
	@FXML
	private ComboBox<String> cityComboBox;
	@FXML
	private TextField minPrice;
	@FXML
	private TextField maxPrice;
	@FXML
	private CheckBox houseCheck;
	@FXML
	private CheckBox apartmentCheck;
	@FXML
	private CheckBox roomCheck;
	@FXML
	private CheckBox villaCheck;
	@FXML
	private VBox offersContainer;

	@FXML
	private BorderPane mainContainer; // Add this field - make sure to wrap your content in a BorderPane in FXM

	private OfferService offerService = new OfferService();
	private ObservableList<Offer> offers = FXCollections.observableArrayList();

	@FXML
	public void initialize() {
		// Initialize cities
		cityComboBox.setItems(FXCollections.observableArrayList("Casablanca", "Rabat", "Marrakech", "Fes", "Tangier",
				"Agadir", "Meknes", "Oujda", "Kenitra", "Tetouan"));

		// Add input validation for price fields
		minPrice.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*(\\.\\d*)?")) {
				minPrice.setText(oldValue);
			}
		});

		maxPrice.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*(\\.\\d*)?")) {
				maxPrice.setText(oldValue);
			}
		});

		// Load initial offers
		loadOffers();
	}

	private void loadOffers() {
		try {
			List<Offer> fetchedOffers = offerService.fetchAllOffers();
			offers.setAll(fetchedOffers);
			displayOffers(FXCollections.observableArrayList(fetchedOffers));
		} catch (SQLException e) {
			e.printStackTrace();
			showError("Error loading offers: " + e.getMessage());
		}
	}

	@FXML
	private void handleSearch() {
		filterOffers();
	}

	@FXML
	private void handleFilterApply() {
		filterOffers();
	}

	private void filterOffers() {
		try {
			String selectedCity = cityComboBox.getValue();
			Double minPriceValue = minPrice.getText().isEmpty() ? null : Double.parseDouble(minPrice.getText());
			Double maxPriceValue = maxPrice.getText().isEmpty() ? null : Double.parseDouble(maxPrice.getText());

			String selectedType = null;
			if (houseCheck.isSelected())
				selectedType = "House";
			else if (apartmentCheck.isSelected())
				selectedType = "Apartment";
			else if (roomCheck.isSelected())
				selectedType = "Room";
			else if (villaCheck.isSelected())
				selectedType = "Villa";

			String searchTerm = searchField.getText().isEmpty() ? null : searchField.getText();

			List<Offer> filteredOffers = offerService.fetchFilteredOffers(selectedCity, minPriceValue, maxPriceValue,
					selectedType, searchTerm);

			displayOffers(FXCollections.observableArrayList(filteredOffers));
		} catch (SQLException e) {
			e.printStackTrace();
			showError("Error filtering offers: " + e.getMessage());
		}
	}

	private void displayOffers(ObservableList<Offer> offersToDisplay) {
	    offersContainer.getChildren().clear();
	    
	    if (offersToDisplay.isEmpty()) {
	        VBox noItemsContainer = new VBox();
	        noItemsContainer.setAlignment(javafx.geometry.Pos.CENTER);
	        noItemsContainer.setSpacing(10);
	        noItemsContainer.setPadding(new javafx.geometry.Insets(20));
	        
	        Label iconLabel = new Label("🔍");
	        iconLabel.setStyle("-fx-font-size: 48px;");
	        
	        Label noItemsLabel = new Label("No items found!");
	        noItemsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
	        
	        Label suggestionLabel = new Label("Try adjusting your search filters or criteria");
	        suggestionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");
	        
	        Button resetButton = new Button("Reset Filters");
	        resetButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20;");
	        resetButton.setOnAction(e -> resetFilters());
	        
	        noItemsContainer.getChildren().addAll(iconLabel, noItemsLabel, suggestionLabel, resetButton);
	        offersContainer.getChildren().add(noItemsContainer);
	    } else {

	        // Create TilePane for the cards
	        TilePane tilePane = new TilePane();
	        tilePane.setPrefColumns(2); // Adjust number of columns as needed
	        tilePane.setHgap(5);
	        tilePane.setVgap(20);
	        tilePane.setPadding(new javafx.geometry.Insets(4));
	        tilePane.setStyle("-fx-background-color: transparent;");
	        
	        
	        // Add cards to the TilePane
	        for (Offer offer : offersToDisplay) {
	            VBox offerCard = createOfferCard(offer);
	            offerCard.setMinWidth(300); // Fixed width for cards
	            offerCard.maxWidth(300);
	            tilePane.getChildren().add(offerCard);
	        }

	        offersContainer.getChildren().add(tilePane);
	    }
	}

	// Add this new method to handle filter reset
	private void resetFilters() {
		// Reset all filter controls
		searchField.clear();
		cityComboBox.setValue(null);
		minPrice.clear();
		maxPrice.clear();
		houseCheck.setSelected(false);
		apartmentCheck.setSelected(false);
		roomCheck.setSelected(false);
		villaCheck.setSelected(false);

		// Reload all offers
		loadOffers();
	}

	private VBox createOfferCard(Offer offer) {
	    VBox card = new VBox(10);
	    card.getStyleClass().add("offer-card");
	    card.setPadding(new javafx.geometry.Insets(4));
	    
	    // Main horizontal container to split images and details
	    HBox mainContainer = new HBox(20);
	    mainContainer.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
	    
	    // Left side: Images container
	    VBox imagesContainer = new VBox(10);
	    imagesContainer.setPrefWidth(300);
	    imagesContainer.setAlignment(javafx.geometry.Pos.CENTER);
	    
	    // Main image with style
	    ImageView mainImage = new ImageView();
	    if (!offer.getImagePaths().isEmpty()) {
	        mainImage.setImage(new Image("file:" + offer.getImagePaths().get(0)));
	    }
	    mainImage.setFitWidth(300);
	    mainImage.setFitHeight(200);
	    mainImage.setPreserveRatio(true);
	    mainImage.getStyleClass().add("main-image");
	    
	    // Small images in horizontal container
	    HBox smallImages = new HBox(10);
	    smallImages.setAlignment(javafx.geometry.Pos.CENTER);
	    
	    for (int i = 1; i < Math.min(3, offer.getImagePaths().size()); i++) {
	        ImageView smallImage = new ImageView(new Image("file:" + offer.getImagePaths().get(i)));
	        smallImage.setFitWidth(90);
	        smallImage.setFitHeight(60);
	        smallImage.setPreserveRatio(true);
	        smallImage.getStyleClass().add("thumbnail-image");
	        smallImages.getChildren().add(smallImage);
	    }
	    
	    imagesContainer.getChildren().addAll(mainImage, smallImages);
	    
	    // Right side: Details container
	    VBox details = new VBox(15);
	    details.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
	    details.setPadding(new javafx.geometry.Insets(10, 0, 10, 0));
	    details.setStyle("-fx-background-color: white;");
	    
	    // Price with styling
	    Label priceLabel = new Label(String.format("%.2f MAD", offer.getPrice()));
	    priceLabel.getStyleClass().addAll("price-label", "bold-text");
	    
	    // Location with icon
	    HBox locationBox = new HBox(10);
	    locationBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
	    Label locationIcon = new Label("📍");
	    Label locationLabel = new Label(offer.getCity() + ", " + offer.getStreet());
	    locationLabel.getStyleClass().add("location-label");
	    locationBox.getChildren().addAll(locationIcon, locationLabel);
	    
	    // Property details in a grid
	    GridPane propertyDetails = new GridPane();
	    propertyDetails.setStyle("-fx-background-color: white");
	    propertyDetails.setHgap(20);
	    propertyDetails.setVgap(10);
	    propertyDetails.add(new Label("Type:"), 0, 0);
	    propertyDetails.add(new Label(offer.getType()), 1, 0);
	    propertyDetails.add(new Label("Rooms:"), 0, 1);
	    propertyDetails.add(new Label(offer.getRooms() + " Rooms"), 1, 1);
	    propertyDetails.add(new Label("Contact:"), 0, 2);
	    propertyDetails.add(new Label(offer.getNumber()), 1, 2);
	    
	    // Status with custom style
	    Label statusLabel = new Label("Status: " + offer.getStatus());
	    statusLabel.getStyleClass().add("status-label");
	    
	    // View Details button with styling
	    Button viewDetailsBtn = new Button("View Details");
	    viewDetailsBtn.getStyleClass().add("view-details-button");
	    viewDetailsBtn.setMaxWidth(Double.MAX_VALUE);
	    viewDetailsBtn.setOnAction(e -> handleViewDetails(offer));
	    
	    details.getChildren().addAll(priceLabel, locationBox, propertyDetails, statusLabel, viewDetailsBtn);
	    
	    // Add everything to main container
	    mainContainer.getChildren().addAll(imagesContainer, details);
	    card.getChildren().add(mainContainer);
	    
	    return card;
	}

	@FXML
	private void handleViewDetails(Offer offer) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/Offer.fxml"));
			Parent offerDetailsView = loader.load();

			OffreController detailsController = loader.getController();
			detailsController.setOffer(offer);

			// Replace the current content with the offer details view
			mainContainer.setCenter(offerDetailsView);

//	            make other sections invisible
			mainContainer.getLeft().setVisible(false);
			mainContainer.getLeft().setManaged(false);
			mainContainer.getTop().setVisible(false);
			mainContainer.getTop().setManaged(false);

		} catch (IOException e) {
			e.printStackTrace();
			showError("Error loading offer details: " + e.getMessage());
		}
	}

	private void showError(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}