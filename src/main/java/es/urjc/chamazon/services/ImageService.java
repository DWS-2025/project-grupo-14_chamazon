package es.urjc.chamazon.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

    private Path createFilePath(String imageName) {
        return FILES_FOLDER.resolve(imageName);
    }

    public String saveImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return null;
        }

        Files.createDirectories(FILES_FOLDER);

        String originalFileName = image.getOriginalFilename();
        Path newFile = createFilePath(originalFileName);
        image.transferTo(newFile);
        return originalFileName;
    }

    public ResponseEntity<Object> createResponseFromImage(String imageName) throws MalformedURLException {
        Path imagePath = FILES_FOLDER.resolve(imageName);

        if (!Files.exists(imagePath)) {
            return ResponseEntity.notFound().build();
        }


        String contentType = null;
        try {
            contentType = Files.probeContentType(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        Resource file = new UrlResource(imagePath.toUri());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, contentType).body(file);
    }

    public void deleteImage(String imageName) throws IOException {
        if (imageName == null || imageName.isEmpty()) {
            return;
        }

        Path imageFile = createFilePath(imageName);
        Files.deleteIfExists(imageFile);
    }
}
