package com.webapp.service;

import com.webapp.domain.AverageColor;
import com.webapp.domain.Space;
import com.webapp.domain.UserAccount;
import com.webapp.domain.UserImage;
import com.webapp.exceptions.FileNotFoundException;
import com.webapp.exceptions.StorageException;
import com.webapp.imageprocessing.Collage;
import com.webapp.imageprocessing.Filter;
import com.webapp.imageprocessing.Frame;
import com.webapp.imageprocessing.Preview;
import com.webapp.json.TagResponse;
import com.webapp.properties.StorageProperties;
import com.webapp.repositories.AverageColorRepository;
import com.webapp.repositories.SpaceRepository;
import com.webapp.repositories.UserImageRepository;

import com.webapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserImageService implements StorageService {

    private final Path rootLocation;
    private final UserImageRepository userImageRepository;
    private final AverageColorRepository averageColorRepository;
    private final UserRepository userRepository;
    private final SpaceRepository spaceRepository;

    @Autowired
    public UserImageService(StorageProperties properties, UserImageRepository userImageRepository,
                            AverageColorRepository averageColorRepository, UserRepository userRepository,
                            SpaceRepository spaceRepository) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.userImageRepository = userImageRepository;
        this.averageColorRepository = averageColorRepository;
        this.userRepository = userRepository;
        this.spaceRepository = spaceRepository;
    }

    public Space getSpace(Long imageId) throws Exception {
        Optional<UserImage> userImage = userImageRepository.findById(imageId);
        if (userImage.isPresent()){
            return userImage.get().getSpace();
        }
        else{
            throw new Exception("cannot find image");
        }
    }

    // get image's tags
    public ArrayList<TagResponse> getTagsByImage(Long imageId) throws Exception {
        Optional<UserImage> userImage = userImageRepository.findById(imageId);
        if (userImage.isPresent()){
            ArrayList<TagResponse>  tagResponses = new ArrayList<>();
            userImage.get().getImageTags()
                           .forEach(imageTag -> tagResponses.add(new TagResponse(imageTag.getTag())));
            return tagResponses;
        }
        else{
            throw new Exception("cannot find image");
        }
    }

    public boolean exists(Long imageId){
        return userImageRepository.existsById(imageId);
    }

    public Optional<UserImage> findById(Long imageId){
        return userImageRepository.findById(imageId);
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
            Path file = load(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new FileNotFoundException(
                        "Could not read file: " + filename);
            }
        }
        catch (IOException e) {
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
            userImageEdited.setAverageColor(userImage.getAverageColor());
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

    public void delete(UserImage userImage){
        userImageRepository.delete(userImage);
    }

    @Override
    public Resource getCollage(List<Long> ids) throws StorageException{
        List<UserImage> userImageList = ids.stream()
                .map(userImageRepository::findById)
                .map(optional -> {
                    if (optional.isPresent())
                        return optional.get();
                    else throw new StorageException("Not enough ids for collage" +
                            " or one of the images not found");
                })
                .collect(Collectors.toList());

        String collagePath = new Collage(rootLocation, userImageList).processing();
        saveInfo(userImageList.get(0).getUser().getId(),
                userImageList.get(0).getSpace().getId(),
                collagePath);

        return this.loadAsResource(Paths.get(collagePath).getFileName().toString());
    }

    @Override
    public void saveInfo(Long user_id, Long space_id, String imagePath) throws IllegalArgumentException,
            StorageException{
        UserAccount user = userRepository.findById(user_id)
                .orElseThrow(() -> new StorageException("No user with such id: " + user_id.toString()));
        String imageName = Paths.get(imagePath).getFileName().toString();

        Optional<Space> spaceRepositoryOptional = spaceRepository.findById(space_id);
        Space space = spaceRepositoryOptional
                .orElseThrow(() -> new StorageException("No space with such id: " + space_id.toString()));

        long countedRgb = Long.parseLong(
                new com.webapp.imageprocessing.AverageColor(rootLocation, imageName).processing().get(0)
        );
        Optional<AverageColor> averageColorOptional = averageColorRepository.findByRgb(countedRgb);
        AverageColor averageColor = averageColorOptional.orElseGet(() -> new AverageColor(countedRgb));

        try {
            UserImage userImage = new UserImage(user, imagePath,
                    new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                    Files.size(Paths.get(imagePath)), averageColor,
                    imageName, space);

            String previewPath = new Preview(rootLocation, userImage).processing();
            userImage.setPreview_path(previewPath);
            userImageRepository.save(userImage);
        } catch (IOException e) {
            throw new StorageException(e.getMessage());
        }
    }

    // We don't use template method here because of Spring DI in controller (UserImageController)
    @Override
    public Resource getPreview(Long id) throws StorageException{
        UserImage userImage = getUserImage(id);

        String previewPath = new Preview(rootLocation, userImage).processing();
        saveInfo(userImage.getUser().getId(), userImage.getSpace().getId(), previewPath);

        return this.loadAsResource(Paths.get(previewPath).getFileName().toString());
    }

    @Override
    public Resource getFilteredImage(Long image_id) throws StorageException{
        UserImage userImage = getUserImage(image_id);

        String filterPath = new Filter(rootLocation, userImage).processing();
        saveInfo(userImage.getUser().getId(), userImage.getSpace().getId(), filterPath);

        return this.loadAsResource(Paths.get(filterPath).getFileName().toString());
    }

    @Override
    public Resource getImageWithFrame(Long image_id) throws StorageException{
        UserImage userImage = getUserImage(image_id);

        String filterPath = new Frame(rootLocation, userImage).processing();
        saveInfo(userImage.getUser().getId(), userImage.getSpace().getId(), filterPath);

        return this.loadAsResource(Paths.get(filterPath).getFileName().toString());
    }

    private UserImage getUserImage(Long image_id) throws StorageException{
        return userImageRepository.findById(image_id)
                .orElseThrow(() -> new StorageException("There's no image with such id: " + image_id.toString()));
    }
}
