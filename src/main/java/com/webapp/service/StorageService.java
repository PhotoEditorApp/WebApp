package com.webapp.service;

import com.webapp.domain.UserImage;
import com.webapp.enums.Filters;
import com.webapp.exceptions.FileNotFoundException;
import com.webapp.exceptions.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file) throws StorageException;

    Stream<Path> loadAll() throws StorageException;

    Path load(String filename);

    Resource loadAsResource(String filename) throws StorageException;

    void deleteAll();

    Resource getResource(Long imageId) throws FileNotFoundException;

    Resource getFrameResource(Long frameId) throws FileNotFoundException;

    void editInfo(Long imageId, String newName) throws StorageException;

    void deleteImage(Long id) throws IOException, FileNotFoundException;

    Resource getCollage(List<Long> ids) throws StorageException;

    void saveInfo(Long userId, Long spaceId, String imagePath) throws IllegalArgumentException,
            StorageException;

    Resource getPreview(Long ids) throws StorageException;

    Resource getFilteredImage(Long imageId, Filters filter) throws StorageException;

    Resource getImageWithFrame(Long imageId, Long frameId) throws StorageException;

    void saveFrameInfo(String name) throws IllegalArgumentException, StorageException;

    Resource getPreviewOfFrameResource(Long id) throws StorageException;

    List<Long> getListOfFramesPreview();

    Resource getPreviewOfPhotoResource(Long id) throws StorageException;

    List<Long> getListOfPhotoPreview();

    Resource getPhotoResource(Long id)
            throws IllegalArgumentException, FileNotFoundException;

    void savePhotoInfo(Long profileId, String name)
            throws IllegalArgumentException, StorageException;

    Resource getPhotoByProfileIdResource(Long profileId) throws StorageException;

    Resource getPreviewOfPhotoByProfileResource(Long profileId) throws StorageException;

    void deleteFrame(Long id) throws FileNotFoundException, IOException;
}
