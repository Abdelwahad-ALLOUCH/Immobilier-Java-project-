package application.model;

public class UserSession {
    private static UserSession instance;
    private int userId;
    private String username;
    private boolean isAdmin;
    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(int userId, String username , boolean isAdmin) {
        this.userId = userId;
        this.username = username;
        this.isAdmin = isAdmin;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
    
    public boolean getIsAdmin() {
    	return isAdmin;
    }
    public void clearSession() {
        userId = 0;
        username = null;
    }
}
