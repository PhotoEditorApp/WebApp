package com.webapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.domain.UserImage;
import com.webapp.enums.AccessType;
import com.webapp.exceptions.FileNotFoundException;
import com.webapp.exceptions.StorageException;
import com.webapp.json.ActionMessage;
import com.webapp.json.FileResponse;
import com.webapp.service.UserImageService;
import com.webapp.service.ImageTagService;
import com.webapp.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/image")
public class UserImageController {

    private final StorageService storageService;
    private final UserImageService imageService;
    private final ImageTagService imageTagService;

    public UserImageController(StorageService storageService, UserImageService imageService,  ImageTagService imageTagService) {
        this.storageService = storageService;
        this.imageService = imageService;
        this.imageTagService = imageTagService;
    }

    // get all tags of image
    @GetMapping("/{image_id}/tag")
    public ResponseEntity<?> getTagsByImage(@PathVariable Long image_id) {
        try {
            return new ResponseEntity<>(imageService.getTagsByImage(image_id), HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // join user tag and image
    @PutMapping("/{image_id}/tag")
    public ResponseEntity<?> c(@PathVariable Long image_id,
                               @RequestParam String tag_name,
                               @RequestParam Long user_id) {
        try {
            imageTagService.save(image_id, tag_name, user_id);
            return new ResponseEntity<>("tag successfully added", HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/get_image_path")
    public ResponseEntity<?> getImageByPath(@RequestParam String path) {
        Resource resource = storageService.loadAsResource(Paths.get(path).getFileName().toString());

        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getURI().getPath())));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/get_image_id")
    public ResponseEntity<?> getImageById(@RequestParam Long id) {
        Resource resource = storageService.getResource(id);

        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getURI().getPath())));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/get_collage")
    public ResponseEntity<?> getCollage(@RequestParam("ids") List<Long> ids) {
        if (!ids.isEmpty()) {
            Resource resource = storageService.getCollage(ids);

            try {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(Files.readAllBytes(Paths.get(resource.getURI().getPath())));
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ActionMessage(e.getMessage()));
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ActionMessage("There's no id to make a collage"));
    }

    @DeleteMapping("/delete_image")
    public ResponseEntity<ActionMessage> deleteImageById(@RequestParam Long id) {
        try {
            storageService.deleteImage(id);
        } catch (FileNotFoundException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }

        return ResponseEntity.ok()
                .body(new ActionMessage("The image was deleted"));
    }

    @PutMapping("/edit_info")
    public ResponseEntity<ActionMessage> editImageInfo(@RequestBody UserImage userImage) {
        try {
            storageService.editInfo(userImage);
        } catch (StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }

        return ResponseEntity.ok()
                .body(new ActionMessage("Image information has been successfully edited"));
    }

    @PostMapping("/upload_image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @RequestParam("info") String userImageString) {
        String name = storageService.store(file);
        try {
            UserImage userImage = new ObjectMapper().readValue(userImageString,
                    UserImage.class);
            storageService.saveInfo(userImage);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        return ResponseEntity.ok()
                .body(new FileResponse(name, uri, file.getContentType(), file.getSize()));
    }

    @PostMapping("/upload_multiple_images")
    public List<FileResponse> uploadMultipleImages(@RequestParam("files") MultipartFile[] files) {
        return null;
//        return Arrays.stream(files)
//                .map(this::uploadImage)
//                .collect(Collectors.toList());
    }

    public StorageService getStorageService() {
        return storageService;
    }



}