package application.core;


import javafx.scene.control.ComboBox;
import java.io.File;
import java.util.List;

public class OfferValidation {
    // Constants for validation rules
    private static final int MAX_IMAGES = 7;
    
    // Description validation
    public static ValidationResult validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return new ValidationResult(false, "Description is required");
        }
        return new ValidationResult(true, "");
    }

    // Price validation
    public static ValidationResult validatePrice(String priceStr) {
        try {
            double price = Double.parseDouble(priceStr.trim());
            if (price <= 0) {
                return new ValidationResult(false, "Price must be greater than 0");
            }
            return new ValidationResult(true, "");
        } catch (NumberFormatException e) {
            return new ValidationResult(false, "Invalid price format");
        }
    }

    // Rooms validation
    public static ValidationResult validateRooms(String roomsStr) {
        try {
            int rooms = Integer.parseInt(roomsStr.trim());
            if (rooms <= 0) {
                return new ValidationResult(false, "Number of rooms must be greater than 0");
            }
            return new ValidationResult(true, "");
        } catch (NumberFormatException e) {
            return new ValidationResult(false, "Invalid number format");
        }
    }

    // Property type validation
    public static ValidationResult validateType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return new ValidationResult(false, "Please select a type");
        }
        return new ValidationResult(true, "");
    }

    // City validation
    public static ValidationResult validateCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            return new ValidationResult(false, "City is required");
        }
        return new ValidationResult(true, "");
    }

    // Street number validation
    public static ValidationResult validateNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            return new ValidationResult(false, "Number is required");
        }
        return new ValidationResult(true, "");
    }

    // Images validation
    public static ValidationResult validateImages(List<File> images) {
        if (images == null || images.isEmpty()) {
            return new ValidationResult(false, "At least one image is required");
        }
        if (images.size() > MAX_IMAGES) {
            return new ValidationResult(false, "Maximum " + MAX_IMAGES + " images allowed");
        }
        return new ValidationResult(true, "");
    }

    // Validation result class to hold both the status and error message
    public static class ValidationResult {
        private final boolean isValid;
        private final String message;

        public ValidationResult(boolean isValid, String message) {
            this.isValid = isValid;
            this.message = message;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getMessage() {
            return message;
        }
    }

    // Utility method to get remaining image slots
    public static int getRemainingImageSlots(List<File> currentImages) {
        return MAX_IMAGES - currentImages.size();
    }
}