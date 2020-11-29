package com.webapp.service;

import com.webapp.domain.UserImage;
import com.webapp.exceptions.FileNotFoundException;
import com.webapp.exceptions.StorageException;
import com.webapp.imageprocessing.Frame;
import com.webapp.json.ActionMessage;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    Resource getResource(Long imageId);

    void editInfo(UserImage userImage) throws StorageException;

    void deleteImage(Long id) throws IOException, FileNotFoundException;

    Resource getCollage(List<Long> ids) throws StorageException;

    void saveInfo(Long user_id, Long space_id, String ImagePath) throws IllegalArgumentException,
            StorageException;

    Resource getPreview(Long ids) throws StorageException;

    Resource getFilteredImage(Long image_id) throws StorageException;

    Resource getImageWithFrame(Long image_id) throws StorageException;
}
