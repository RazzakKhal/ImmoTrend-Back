package immotrend.com.immotrend.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class ImageServiceImpl implements ImageService{
    private final String UPLOAD_DIRECTORY = "upload";

    /**
     * rend le nom d'une image unique à l'aide du timestamp et l'enregistre dans le UPLOAD_DIRECTORY
     * @param image
     * @return
     * @throws IOException
     */
    @Override
    public String saveFileOnServerAndReturnFileUrl(MultipartFile image) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = this.creationOfDirectoryIfNotExistAndReturnPath().resolve(fileName);
        Files.copy(image.getInputStream(), filePath);

        return UPLOAD_DIRECTORY + "/" + fileName;
    }

    /**
     * créer le dossier si il n'existe pas et retourne son chemin
     * @return
     * @throws IOException
     */
    private Path creationOfDirectoryIfNotExistAndReturnPath() throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        return uploadPath;
    }



}