package AI_PRJ.WEBAPP.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    private final String uploadDir = "src/main/resources/static/images/";

    public String storeFile(MultipartFile file) throws IOException {
        // Normalize file name
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + fileExtension;

        Path copyLocation = Paths.get(uploadDir + fileName);
        Files.copy(file.getInputStream(), copyLocation);

        return "/images/" + fileName;
    }
}