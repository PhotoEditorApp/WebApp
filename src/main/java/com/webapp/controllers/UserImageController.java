package com.webapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.domain.UserImage;
import com.webapp.exceptions.FileNotFoundException;
import com.webapp.exceptions.StorageException;
import com.webapp.json.ActionMessage;
import com.webapp.json.FileResponse;
import com.webapp.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
public class UserImageController {

    private final StorageService storageService;

    public UserImageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/images/get_image_name")
    public ResponseEntity<Resource> getImageByName(@RequestParam String filename) {
        Resource resource = storageService.loadAsResource(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/images/get_image_id")
    public ResponseEntity<Resource> getImageById(@RequestParam Long id){
        Resource resource = storageService.getResource(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/images/get_collage")
    public ResponseEntity<?> getCollage(@RequestParam("ids") List<Long> ids){
        if (!ids.isEmpty()){
            Resource resource = storageService.getCollage(ids);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ActionMessage("There's no id to make collage"));
    }

    @DeleteMapping("/image/delete_image")
    public ResponseEntity<ActionMessage> deleteImageById(@RequestParam Long id){
        try {
            storageService.deleteImage(id);
        }catch (FileNotFoundException | IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }

        return ResponseEntity.ok()
                .body(new ActionMessage("The image was deleted"));
    }

    @PutMapping("/image/edit_info")
    public ResponseEntity<ActionMessage> editImageInfo(@RequestBody UserImage userImage){
        try {
            storageService.editInfo(userImage);
        }catch (StorageException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }

        return ResponseEntity.ok()
                .body(new ActionMessage("Image information has been successfully edited"));
    }

    @PostMapping("/images/upload_image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                    @RequestParam("info") String userImageString) {
        String name = storageService.store(file);
        try {
            UserImage userImage = new ObjectMapper().readValue(userImageString,
                    UserImage.class);
            storageService.saveInfo(userImage);
        }catch (IllegalArgumentException | JsonProcessingException e){
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
}
