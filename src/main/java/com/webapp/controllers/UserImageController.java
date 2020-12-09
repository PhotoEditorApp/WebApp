package com.webapp.controllers;

import com.webapp.domain.UserImage;
import com.webapp.enums.Filters;
import com.webapp.exceptions.FileNotFoundException;
import com.webapp.exceptions.StorageException;
import com.webapp.json.ActionMessage;
import com.webapp.json.FileResponse;
import com.webapp.service.ImageTagService;
import com.webapp.service.StorageService;
import com.webapp.service.TagService;
import com.webapp.service.UserImageService;
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

@RestController
@RequestMapping("/image")
public class UserImageController {

    private final StorageService storageService;
    private final UserImageService imageService;
    private final ImageTagService imageTagService;
    private final TagService tagService;

    public UserImageController(StorageService storageService, UserImageService imageService, ImageTagService imageTagService, TagService tagService) {
        this.storageService = storageService;
        this.imageService = imageService;
        this.imageTagService = imageTagService;
        this.tagService = tagService;
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


    // get all ratings of image
    @GetMapping("/{image_id}/rating")
    public ResponseEntity<?> getImageRatings(@PathVariable Long image_id) {
        try {
            return new ResponseEntity<>(imageService.getRatingsByImage(image_id), HttpStatus.OK);

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
                            "attachment; filename=\"" + resource.getFilename() + "\"").body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
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
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> applyFilterByImageId(@RequestParam Long id,
                                                  @RequestParam Filters filter) {
        byte[] bytesToSend = storageService.getFilteredImage(id, filter);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"filtered_img_%d\"", id))
                .body(bytesToSend);
    }

    @GetMapping("/frame")
    public ResponseEntity<?> addFrameByImageId(@RequestParam Long id,
                                               @RequestParam Long frameId) {
        byte[] bytesToSend = storageService.getImageWithFrame(id, frameId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"frame_%d_%d\"", id, frameId))
                .body(bytesToSend);
    }

    @GetMapping("/get_preview_img_id")
    public ResponseEntity<?> getPreviewByImageId(@RequestParam Long id) {
        Resource resource = storageService.getPreview(id);

        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
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
                        .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ActionMessage(e.getMessage()));
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ActionMessage("There's no id to make a collage"));
    }

    // TODO нужно удалять и теги, связанные с изображением
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
    public ResponseEntity<ActionMessage> editImageInfo(@RequestParam Long imageId,
                                                       @RequestParam String newName) {
        try {
            storageService.editInfo(imageId, newName);
        } catch (StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }

        return ResponseEntity.ok()
                .body(new ActionMessage("Image information has been successfully edited"));
    }

    @PostMapping("/upload_image")
    public ResponseEntity<?> uploadImage(@RequestParam("user_id") Long user_id, @RequestParam("space_id") Long space_id,
                                         @RequestParam("file") MultipartFile file) {
        String name = storageService.store(file);
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        try {
            storageService.saveInfo(user_id, space_id, "uploads/" + name);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }

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

    @PostMapping("/upload_frame")
    public ResponseEntity<?> uploadFrame(@RequestParam("frame") MultipartFile file) {
        String name = storageService.store(file);
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        try {
            storageService.saveFrameInfo(name);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }

        return ResponseEntity.ok()
                .body(new FileResponse(name, uri, file.getContentType(), file.getSize()));
    }


    @GetMapping("/get_frame_id")
    public ResponseEntity<?> getFrameById(@RequestParam Long id) {
        Resource resource = storageService.getFrameResource(id);

        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }
}