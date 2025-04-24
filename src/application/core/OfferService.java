package application.core;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.model.Database;
import application.model.Offer;
import application.model.UserSession;

public class OfferService {
    private static final String INSERT_OFFER_SQL = 
        "INSERT INTO offre (description, id_user, nbr_chambre, numero, price, street, type, ville ,creation_date) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ? , CURRENT_DATE()	)";
    
    private static final String INSERT_IMAGE_SQL = 
        "INSERT INTO images (id_offre, path) VALUES (?, ?)";

    private static final String CALL_FETCH_ALL_OFFERS = "{CALL sp_fetch_all_offers()}";
    private static final String CALL_FETCH_FILTERED_OFFERS = "{CALL sp_fetch_filtered_offers(?, ?, ?, ?, ?)}";
    
//    pour publier une offre
    public void publishOffer(String description, Integer nbrChambre, String numero, 
                           Double price, String street, String type, String ville, 
                           List<File> images) throws SQLException, IOException {
        
        Connection conn = Database.getConnection();
        PreparedStatement offerStmt = null;
        PreparedStatement imageStmt = null;
        
        try {
            // Start transaction
            conn.setAutoCommit(false);
            
            // Get current user ID from session
            int userId = UserSession.getInstance().getUserId();
            
            // Insert offer
            offerStmt = conn.prepareStatement(INSERT_OFFER_SQL, Statement.RETURN_GENERATED_KEYS);
            offerStmt.setString(1, description);
            offerStmt.setInt(2, userId);
            offerStmt.setInt(3, nbrChambre);
            offerStmt.setString(4, numero);
            offerStmt.setDouble(5, price);
            offerStmt.setString(6, street);
            offerStmt.setString(7, type);
            offerStmt.setString(8, ville);
            
            offerStmt.executeUpdate();
            
            // Get the generated offer ID
            ResultSet generatedKeys = offerStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int offerId = generatedKeys.getInt(1);
                
                // Save images to filesystem and get paths
                List<String> imagePaths = ImageStorageUtil.saveImages(images);
                
                // Save image paths to database
                imageStmt = conn.prepareStatement(INSERT_IMAGE_SQL);
                for (String imagePath : imagePaths) {
                    imageStmt.setInt(1, offerId);
                    imageStmt.setString(2, imagePath);
                    imageStmt.executeUpdate();
                }
            } else {
                throw new SQLException("Failed to get generated offer ID");
            }
            
            // Commit transaction
            conn.commit();
            
        } catch (SQLException | IOException e) {
            // Rollback transaction on error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (imageStmt != null) {
                try {
                    imageStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (offerStmt != null) {
                try {
                    offerStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
//    pour fetcher les offres : 
    public List<Offer> fetchAllOffers() throws SQLException {
        Connection conn = Database.getConnection();
        CallableStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareCall(CALL_FETCH_ALL_OFFERS);
            rs = stmt.executeQuery();
            
            return buildOffersFromResultSet(rs);
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }

    public List<Offer> fetchFilteredOffers(String city, Double minPrice, Double maxPrice, 
                                         String type, String searchTerm) throws SQLException {
        Connection conn = Database.getConnection();
        CallableStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareCall(CALL_FETCH_FILTERED_OFFERS);
            
            // Set parameters
            stmt.setString(1, city);
            
            if (minPrice != null) {
                stmt.setDouble(2, minPrice);
            } else {
                stmt.setNull(2, Types.DECIMAL);
            }
            
            if (maxPrice != null) {
                stmt.setDouble(3, maxPrice);
            } else {
                stmt.setNull(3, Types.DECIMAL);
            }
            
            stmt.setString(4, type);
            stmt.setString(5, searchTerm);
            
            rs = stmt.executeQuery();
            return buildOffersFromResultSet(rs);
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }

    private List<Offer> buildOffersFromResultSet(ResultSet rs) throws SQLException {
        List<Offer> offers = new ArrayList<>();
        Map<Long, Offer> offerMap = new HashMap<>();
        
        while (rs.next()) {
            Long offerId = rs.getLong("id");
            Offer offer = offerMap.get(offerId);
            
            if (offer == null) {
                offer = new Offer();
                offer.setId(offerId);
                offer.setDescription(rs.getString("description"));
                offer.setUserId(rs.getString("id_user"));
                offer.setRooms(rs.getInt("nbr_chambre"));
                offer.setNumber(rs.getString("numero"));
                offer.setPrice(rs.getDouble("price"));
                offer.setStreet(rs.getString("street"));
                offer.setType(rs.getString("type"));
                offer.setCity(rs.getString("ville"));
                offer.setCreationDate(rs.getString("creation_date"));
                offer.setStatus(rs.getString("status"));
                offer.setImagePaths(new ArrayList<>());
                
                offerMap.put(offerId, offer);
                offers.add(offer);
            }
            
            // Add image path if it exists and isn't already in the list
            String imagePath = rs.getString("path");
            if (imagePath != null && !offer.getImagePaths().contains(imagePath)) {
                offer.getImagePaths().add(imagePath);
            }
        }
        
        return offers;
    }

}