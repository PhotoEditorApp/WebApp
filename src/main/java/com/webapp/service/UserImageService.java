package com.webapp.service;

import com.webapp.domain.AverageColor;
import com.webapp.domain.Space;
import com.webapp.domain.UserImage;
import com.webapp.exceptions.FileNotFoundException;
import com.webapp.exceptions.StorageException;
import com.webapp.json.TagResponse;
import com.webapp.properties.StorageProperties;
import com.webapp.repositories.AverageColorRepository;
import com.webapp.repositories.UserImageRepository;

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

    @Autowired
    public UserImageService(StorageProperties properties, UserImageRepository userImageRepository,
                            AverageColorRepository averageColorRepository) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.userImageRepository = userImageRepository;
        this.averageColorRepository = averageColorRepository;
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
                    else throw new StorageException("Not enough ids for collage");
                })
                .collect(Collectors.toList());

        String collagePath = concatenateImages(userImageList);
        String collageName = Paths.get(collagePath).getFileName().toString();

        // todo figure out fow to count average color
        int countedRgb = 0;
        Optional<AverageColor> averageColorOptional = averageColorRepository.findByRgb(countedRgb);
        AverageColor averageColor = averageColorOptional.orElseGet(() -> new AverageColor(countedRgb));

        UserImage collage = new UserImage(userImageList.get(0).getUser(), collagePath,
                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                userImageList.get(0).getSize() * userImageList.size(), averageColor,
                collageName);

        userImageRepository.save(collage);

        return this.loadAsResource(collageName);
    }

    @Override
    public void saveInfo(UserImage userImage) throws IllegalArgumentException{
        userImageRepository.save(userImage);
    }

    public String concatenateImages(List<UserImage> userImageList) throws StorageException{
        String collageName = String.format("collage%d.png", new Random().nextInt());
        String collagePath = rootLocation.resolve(collageName).normalize().toAbsolutePath().toString();
        String script = Paths.get("scripts/make_collage.py").normalize().toAbsolutePath().toString();
        List<String> command = userImageList.stream()
                            .map(UserImage::getName)
                            .map(path -> rootLocation.resolve(path).normalize().toAbsolutePath().toString())
                            .collect(Collectors.toList());

        command.add(0, script);
        if (System.getProperty("os.name").startsWith("Windows"))
            command.add(0, "C:\\python3\\python3.exe");
        else
            command.add(0, "python3");

        command.add(collagePath);

        try {
            Process process = new ProcessBuilder(command).start();
            String errorStr = new BufferedReader(new InputStreamReader(process.getErrorStream()))
                    .lines()
                    .toString();

            if (!errorStr.contains("java.util.stream.ReferencePipeline"))
                throw new IOException(errorStr);

            process.waitFor();

        } catch (IOException | InterruptedException e) {
            throw new StorageException(e.getMessage());
        }

        return rootLocation.resolve(collageName).normalize().toString();
    }
}
