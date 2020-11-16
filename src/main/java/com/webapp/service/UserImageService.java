package com.webapp.service;

import com.webapp.domain.UserImage;
import com.webapp.exceptions.FileNotFoundException;
import com.webapp.exceptions.StorageException;
import com.webapp.opencv.OperationsWithImages;
import com.webapp.properties.StorageProperties;
import com.webapp.repositories.UserImageRepository;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserImageService implements StorageService {

    private final Path rootLocation;
    private final UserImageRepository userImageRepository;

    @Autowired
    public UserImageService(StorageProperties properties, UserImageRepository userImageRepository) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.userImageRepository = userImageRepository;
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }

        return filename;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new FileNotFoundException(
                        "Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public Resource getResource(Long imageId){
        Optional<UserImage> userImage = userImageRepository.findById(imageId);

        if (userImage.isPresent()){
            return this.loadAsResource(userImage.get().getName());
        }else {
            throw new FileNotFoundException(
                    "Could not find file by id: " + imageId.toString());
        }
    }

    @Override
    public void editInfo(UserImage userImage) throws StorageException{
        Optional<UserImage> userImageOriginal = userImageRepository.findById(userImage.getId());

        if (userImageOriginal.isPresent()){
            UserImage userImageEdited = userImageOriginal.get();
            userImageEdited.setAverageColorId(userImage.getAverageColorId());
            userImageEdited.setCreateTime(userImage.getCreateTime());
            userImageEdited.setModifiedTime(userImage.getModifiedTime());
            userImageEdited.setName(userImage.getName());
            userImageEdited.setUser(userImage.getUser());
            userImageEdited.setPath(userImage.getPath());

            userImageRepository.save(userImageEdited);
        }else {
            throw new StorageException(
                    "Could not find user by id: " + userImage.getId().toString());
        }
    }

    @Override
    public void deleteImage(Long id) throws IOException, FileNotFoundException{
        Optional<UserImage> userImageOptional = userImageRepository.findById(id);

        if (userImageOptional.isPresent()){
            UserImage userImage = userImageOptional.get();

            Files.delete(Paths.get(rootLocation.resolve(userImage.getName()).toString()));

            userImageRepository.delete(userImage);
        } else {
            throw new FileNotFoundException("File is not found by id: " + id.toString());
        }
    }

    @Override
    public Resource getCollage(List<Long> ids) throws FileNotFoundException{
        List<UserImage> userImageList = ids.stream()
                .map(userImageRepository::findById)
                .map(optional -> {
                    if (optional.isPresent())
                        return optional.get();
                    else throw new FileNotFoundException("Not enough ids for collage");
                })
                .collect(Collectors.toList());

        String collagePath = concatenateImages(userImageList);
        // todo figure out fow to count average color
        UserImage collage = new UserImage(userImageList.get(0).getUser(), collagePath,
                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                userImageList.get(0).getSize() * userImageList.size(), 0L,
                Paths.get(collagePath).getFileName().toString());

        userImageRepository.save(collage);
        return this.loadAsResource(collage.getName());
    }

    @Override
    public void saveInfo(UserImage userImage) throws IllegalArgumentException{
        userImageRepository.save(userImage);
    }

    public String concatenateImages(List<UserImage> userImageList){
//        Mat collage = new Mat();
//        Core.hconcat(userImageList.stream()
//                .map(img -> Imgcodecs.imread(img.getPath()))
//                .collect(Collectors.toList()), collage);
//
//        String path =
//                "collage" + userImageList.stream()
//                .map(userImage -> userImage.getId().toString())
//                .reduce(String::concat);
//
//        Imgcodecs.imwrite(rootLocation.resolve(path).toString(), collage);
//
//        return path;
        return null;
    }
}
