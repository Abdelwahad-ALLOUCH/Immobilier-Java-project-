package application.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connection = null;

    // Private constructor to prevent instantiation
    private Database() {}

    @SuppressWarnings("exports")
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Establish the connection
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/immobilier", "root", "");
                System.out.println("Database connection established!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    
    
    
    
    

	public static void closeConnection() {
		// TODO Auto-generated method stub
		if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}
}
