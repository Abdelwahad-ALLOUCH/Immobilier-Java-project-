package application.core;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        // Vérifie si l'email n'est pas null et correspond au format standard
        return email != null && email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    public static boolean isEmailEmpty(String email) {
        // Vérifie si l'email est vide
        return email == null || email.isEmpty();
    }

    public static boolean isValidPassword(String password) {
        // Vérifie si le mot de passe est valide (longueur minimum de 8 caractères)
        return password != null && password.length() >= 8;
    }

    public static boolean isPasswordEmpty(String password) {
        // Vérifie si le mot de passe est vide
        return password == null || password.isEmpty();
    }

    public static boolean isValidName(String name) {
        // Vérifie si le nom est valide (longueur minimum de 2 caractères)
        return name != null && name.length() >= 2;
    }

    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        // Vérifie si les mots de passe correspondent
        return password != null && password.equals(confirmPassword);
    }
}
