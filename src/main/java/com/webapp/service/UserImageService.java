package com.webapp.service;

import com.webapp.domain.*;
import com.webapp.domain.AverageColor;
import com.webapp.domain.Frame;
import com.webapp.domain.*;
import com.webapp.domain.Frame;
import com.webapp.enums.Filters;
import com.webapp.exceptions.FileNotFoundException;
import com.webapp.exceptions.StorageException;
import com.webapp.imageprocessing.*;
import com.webapp.json.ImageRatingResponse;
import com.webapp.json.TagResponse;
import com.webapp.properties.StorageProperties;
import com.webapp.repositories.*;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private final FrameRepository frameRepository;

    @Autowired
    public UserImageService(StorageProperties properties, UserImageRepository userImageRepository,
                            AverageColorRepository averageColorRepository, UserRepository userRepository,
                            SpaceRepository spaceRepository, FrameRepository frameRepository) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.userImageRepository = userImageRepository;
        this.averageColorRepository = averageColorRepository;
        this.userRepository = userRepository;
        this.spaceRepository = spaceRepository;
        this.frameRepository = frameRepository;
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

    public void updateImageRating(Long imageId) throws Exception {
        Float rating = (float) 0;

        Optional<UserImage> userImage = findById(imageId);
        userImage.orElseThrow(() -> new Exception("cannot find image"));

        // calculate rating
        int size = userImage.get().getRatings().size();
        if (size == 0){
            // set it null if there aren't ratings
            userImage.get().setRating(null);
        }
        else {
            for (ImageRating imageRating : userImage.get().getRatings()) {
                rating += imageRating.getRating();
            }
            rating = rating / size;

            userImage.get().setRating(rating);
        }

        // save rating or null
        userImageRepository.save(userImage.get());
    }

    // return list of Ratings of Image
    public ArrayList<ImageRatingResponse> getRatingsByImage(Long imageId) throws Exception {
        Optional<UserImage> userImage = userImageRepository.findById(imageId);
        // check if empty
        userImage.orElseThrow(()-> new Exception("cannot fin image"));

        // convert ImageRating -> ImageRatingResponse, then aggregate it into list
        // and return
        return (ArrayList<ImageRatingResponse>) userImage.get().getRatings()
                .stream()
                .map(imageRating -> new ImageRatingResponse(
                        imageRating.getUserAccount().getId(),
                        imageRating.getImage().getId(),
                        imageRating.getRating()
                ))
                .collect(Collectors.toList());
    }



    // get image's tags
    public ArrayList<String> getTagsByImage(Long imageId) throws Exception {
        Optional<UserImage> userImage = userImageRepository.findById(imageId);
        if (userImage.isPresent()){
            ArrayList<String>  tags = new ArrayList<>();
            userImage.get().getTags()
                           .forEach(imageTag -> tags.add(imageTag.getImageTagId().getTagName()));
            return tags;
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
    public void editInfo(Long imageId, String newName) throws StorageException{
        UserImage userImageOriginal = userImageRepository.findById(imageId)
                .orElseThrow(() -> new StorageException("Could not find user by id: " + imageId.toString()));

        Path oldImg = Paths.get(rootLocation.resolve(userImageOriginal.getName()).toString());
        Path newImg = Paths.get(rootLocation.resolve(newName).toString());
        try {
            Files.move(oldImg, newImg);
        } catch (IOException e) {
            throw new StorageException(e.getMessage());
        }

        userImageOriginal.getSpace().setModifiedTime(new Date(System.currentTimeMillis()));
        userImageOriginal.setName(newName);
        userImageOriginal.setPath(rootLocation.resolve(newName).normalize().toString());

        userImageRepository.save(userImageOriginal);
    }

    // this method duplicates getResource because these query applies to another tables
    // there's no handy way to combine these methods together
    @Override
    public Resource getFrameResource(Long frameId){
        Optional<Frame> frame = frameRepository.findById(frameId);

        if (frame.isPresent()){
            return this.loadAsResource(frame.get().getName());
        }else {
            throw new FileNotFoundException(
                    "Could not find file by id: " + frameId.toString());
        }
    }

    @Override
    public void deleteImage(Long id) throws IOException, FileNotFoundException{
        Optional<UserImage> userImageOptional = userImageRepository.findById(id);

        if (userImageOptional.isPresent()){
            UserImage userImage = userImageOptional.get();

            userImage.getSpace().setModifiedTime(new Date(System.currentTimeMillis()));
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
    public void saveInfo(Long userId, Long spaceId, String imagePath) throws IllegalArgumentException,
            StorageException{
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("No user with such id: " + userId.toString()));
        String imageName = Paths.get(imagePath).getFileName().toString();

        Optional<Space> spaceRepositoryOptional = spaceRepository.findById(spaceId);
        Space space = spaceRepositoryOptional
                .orElseThrow(() -> new StorageException("No space with such id: " + spaceId.toString()));

        try {
            long countedRgb = Long.parseLong(
                    new com.webapp.imageprocessing.AverageColor(rootLocation, imageName).processing().get(0)
            );
            Optional<AverageColor> averageColorOptional = averageColorRepository.findByRgb(countedRgb);
            AverageColor averageColor = averageColorOptional.orElseGet(() -> new AverageColor(countedRgb));

            Date time = new Date(System.currentTimeMillis());
            UserImage userImage = new UserImage(user, imagePath, time, time,
                    Files.size(Paths.get(imagePath)), averageColor,
                    imageName, space);

            String previewPath = new Preview(rootLocation, userImage).processing();
            userImage.setPreview_path(previewPath);
            userImage.getSpace().setModifiedTime(new Date(System.currentTimeMillis()));

            userImageRepository.save(userImage);
        } catch (IOException | IndexOutOfBoundsException e) {
            throw new StorageException(e.getLocalizedMessage());
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
    public byte[] getFilteredImage(Long imageId, Filters filter) throws StorageException{
        UserImage userImage = getUserImage(imageId);

        String filterPath = switch (filter) {
            case WB -> new WhiteAndBlackFilter(rootLocation, userImage).processing();
            case SHARP -> new SharpeningFilter(rootLocation, userImage).processing();
            case BLUR -> new BlurFilter(rootLocation, userImage).processing();
        };

        try {
            byte[] bytesToSend = Files.readAllBytes(Paths.get(filterPath));
            Files.delete(Paths.get(filterPath));

            return bytesToSend;
        } catch (IOException e) {
            throw new StorageException(e.getLocalizedMessage());
        }
    }

    @Override
    public byte[] getImageWithFrame(Long imageId, Long frameId) throws StorageException{
        UserImage userImage = getUserImage(imageId);
        Frame frame = getFrame(frameId);

        String filterPath = new com.webapp.imageprocessing.Frame(rootLocation, userImage, frame)
                .processing();

        try {
            byte[] bytesToSend = Files.readAllBytes(Paths.get(filterPath));
            Files.delete(Paths.get(filterPath));

            return bytesToSend;
        } catch (IOException e) {
            throw new StorageException(e.getMessage());
        }
    }

    @Override
    public void saveFrameInfo(String name) {
        Frame frame = new Frame(name);
        frame.setPath(rootLocation.resolve(name).toString());

        frameRepository.save(frame);
    }

    private UserImage getUserImage(Long imageId) throws StorageException{
        return userImageRepository.findById(imageId)
                .orElseThrow(() -> new StorageException("There's no image with such id: " + imageId.toString()));
    }

    private Frame getFrame(Long frameId) throws StorageException{
        return frameRepository.findById(frameId)
                .orElseThrow(() -> new StorageException("There's no frame with such id: " + frameId.toString()));
    }
}
