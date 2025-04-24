package application.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Offer {
    private Long id;
    private String description;
    private double price;
    private int rooms;
    private String type;
    private String city;
    private String street;
    private String number;
    private List<String> imagePaths;
    private String userId; // To link the offer with the user who created it
    private String creationDate;
    private String status; // e.g., "active", "sold", "rented"

    // Default constructor
    public Offer() {
        this.imagePaths = new ArrayList<>();
        this.status = "active";
    }

    // Constructor with all fields
    public Offer(Long id, String description, double price, int rooms, 
                String type, String city, String street, String number, 
                List<String> imagePaths, String userId, String creationDate, String status) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.rooms = rooms;
        this.type = type;
        this.city = city;
        this.street = street;
        this.number = number;
        this.imagePaths = imagePaths != null ? imagePaths : new ArrayList<>();
        this.userId = userId;
        this.creationDate = creationDate;
        this.status = status != null ? status : "active";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths != null ? imagePaths : new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Add a single image path
    public void addImagePath(String imagePath) {
        if (this.imagePaths == null) {
            this.imagePaths = new ArrayList<>();
        }
        this.imagePaths.add(imagePath);
    }

    // Remove a single image path
    public boolean removeImagePath(String imagePath) {
        return this.imagePaths != null && this.imagePaths.remove(imagePath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Double.compare(offer.price, price) == 0 &&
               rooms == offer.rooms &&
               Objects.equals(id, offer.id) &&
               Objects.equals(description, offer.description) &&
               Objects.equals(type, offer.type) &&
               Objects.equals(city, offer.city) &&
               Objects.equals(street, offer.street) &&
               Objects.equals(number, offer.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, price, rooms, type, city, street, number);
    }

    @Override
    public String toString() {
        return "Offer{" +
               "id=" + id +
               ", description='" + description + '\'' +
               ", price=" + price +
               ", rooms=" + rooms +
               ", type='" + type + '\'' +
               ", city='" + city + '\'' +
               ", street='" + street + '\'' +
               ", number='" + number + '\'' +
               ", numberOfImages=" + (imagePaths != null ? imagePaths.size() : 0) +
               ", userId='" + userId + '\'' +
               ", creationDate='" + creationDate + '\'' +
               ", status='" + status + '\'' +
               '}';
    }
}