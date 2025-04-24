package application.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageStorageUtil {
    private static final String IMAGE_DIRECTORY = "resources/images/";
    
    /**
     * Saves images to the resources directory and returns their paths
     * @param images List of image files to save
     * @return List of relative paths where images were saved
     * @throws IOException if there's an error saving the images
     */
    public static List<String> saveImages(List<File> images) throws IOException {
        List<String> savedImagePaths = new ArrayList<>();
        
        // Create directory if it doesn't exist
        File directory = new File(IMAGE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        for (File imageFile : images) {
            String uniqueFileName = generateUniqueFileName(imageFile.getName());
            String relativePath = IMAGE_DIRECTORY + uniqueFileName;
            
            // Create the destination path
            Path destinationPath = Paths.get(relativePath);
            
            // Copy the file to the destination
            Files.copy(
                imageFile.toPath(),
                destinationPath,
                StandardCopyOption.REPLACE_EXISTING
            );
            
            // Add the relative path to our list
            savedImagePaths.add(relativePath);
        }
        
        return savedImagePaths;
    }
    
    /**
     * Generates a unique filename using timestamp and UUID
     * @param originalFileName original name of the file
     * @return unique filename
     */
    private static String generateUniqueFileName(String originalFileName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uniqueID = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFileName);
        
        return timestamp + "_" + uniqueID + extension;
    }
    
    /**
     * Extracts file extension from filename
     * @param fileName name of the file
     * @return file extension including the dot
     */
    private static String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // No extension found
        }
        return fileName.substring(lastIndexOf);
    }
    
    /**
     * Deletes an image from the resources directory
     * @param imagePath relative path of the image to delete
     * @return true if deletion was successful
     */
    public static boolean deleteImage(String imagePath) {
        try {
            Path path = Paths.get(imagePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}