package es.urjc.chamazon.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {
    private static final Path IMAGES_FOLDER = Paths.get("src/main/resources/static/images");
    private static final String IMAGES_URL_PATH = "/images/";

    public ImageService() {
        createImagesDirectory();
    }

    private void createImagesDirectory() {
        try {
            Files.createDirectories(IMAGES_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de imágenes", e);
        }
    }

    public String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String fileName = generateFileName(file);
        Path imagePath = IMAGES_FOLDER.resolve(fileName);
        Files.copy(file.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        
        return IMAGES_URL_PATH + fileName;
    }

    private String generateFileName(MultipartFile file) {
        return System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("\\s+", "_");
    }

    public void deleteImage(String fileName) {
        if (fileName != null && fileName.startsWith(IMAGES_URL_PATH)) {
            fileName = fileName.substring(IMAGES_URL_PATH.length());
        }
        
        try {
            Path imagePath = IMAGES_FOLDER.resolve(fileName);
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la imagen: " + fileName, e);
        }
    }
}