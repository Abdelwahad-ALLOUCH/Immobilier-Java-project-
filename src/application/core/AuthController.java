package application.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.model.Database;
import application.model.UserSession;

public class AuthController {
	
    public static boolean login(String email, String password) {
    	
        String query = "SELECT id , isAdmin FROM users WHERE email = ? AND password = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
            	
                if (rs.next()) {
                    // Store user session
                    int userId = rs.getInt("id");
                    boolean isAdmin = rs.getBoolean("isAdmin");
                    UserSession.getInstance().setUser(userId, email , isAdmin);
                    return true;
                }
            }
        } catch (SQLException e) {
        	
            e.printStackTrace();
        }
        return false;
    }

    public static boolean signup(String username,String email, String password) {
        String query = "INSERT INTO users (name, password , email , date_inscription) VALUES (?, ? , ?, now())";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

