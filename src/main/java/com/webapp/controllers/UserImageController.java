package com.webapp.controllers;

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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/image")
public class UserImageController {

    private final StorageService storageService;
    private final UserImageService imageService;

    public UserImageController(StorageService storageService, UserImageService imageService, ImageTagService imageTagService, TagService tagService) {
        this.storageService = storageService;
        this.imageService = imageService;
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
            storageService.editInfo(imageId, new String(newName.getBytes(), StandardCharsets.UTF_8));
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
    public List<FileResponse> uploadMultipleImages(@RequestParam("user_ids") Long[] user_id,
                                                   @RequestParam("space_ids") Long[] space_id,
                                                   @RequestParam("files") MultipartFile[] files) {
        List<FileResponse> fileResponses = new ArrayList<>();
        for (int i = 0; i < Math.min(Math.min(user_id.length, files.length), space_id.length); i++){
            fileResponses.add((FileResponse) this.uploadImage(user_id[i], space_id[i], files[i]).getBody());
        }
        return fileResponses;
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
        } catch (IOException | FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/frame_preview_id")
    public ResponseEntity<?> getPreviewOfFrameById(@RequestParam Long id) {
        byte[] bytes = storageService.getPreviewOfFrameResource(id);

        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            String.format("attachment; filename=preview_%d", id))
                    .body(bytes);
        } catch (StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/frame_previews_ids")
    public ResponseEntity<List<Long>> getPreviewOfFramesId() {
        List<Long> PreviewListOfId = storageService.getListOfFramesPreview();

        return ResponseEntity.ok()
                .body(PreviewListOfId);
    }

    @GetMapping("/photo_preview_id")
    public ResponseEntity<?> getPreviewOfPhotoById(@RequestParam Long PhotoPreviewId) {
        byte[] bytes = storageService.getPreviewOfPhotoResource(PhotoPreviewId);

        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            String.format("attachment; filename=preview_photo_%d", PhotoPreviewId))
                    .body(bytes);
        } catch (StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/photo_preview_profile_id")
    public ResponseEntity<?> getPreviewOfPhotoByProfileId(@RequestParam("profile_id") Long profileId) {
        byte[] bytes = storageService.getPreviewOfPhotoByProfileResource(profileId);

        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            String.format("attachment; filename=preview_photo_%d", profileId))
                    .body(bytes);
        } catch (StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/photo_previews_ids")
    public ResponseEntity<List<Long>> getPreviewOfPhotosId() {
        List<Long> PreviewListOfId = storageService.getListOfPhotoPreview();

        return ResponseEntity.ok()
                .body(PreviewListOfId);
    }

    @PostMapping("/photo")
    public ResponseEntity<?> uploadPhoto(@RequestParam("profile_id") Long id,
                                         @RequestParam("photo") MultipartFile file) {
        String name = storageService.store(file);
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        try {
            storageService.savePhotoInfo(id, name);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }

        return ResponseEntity.ok()
                .body(new FileResponse(name, uri, file.getContentType(), file.getSize()));
    }


    @GetMapping("/photo_id")
    public ResponseEntity<?> getPhotoById(@RequestParam Long photoId) {
        Resource resource = storageService.getPhotoResource(photoId);

        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException | FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/photo_profile_id")
    public ResponseEntity<?> getPhotoByProfileId(@RequestParam Long profileId) {
        Resource resource = storageService.getPhotoByProfileIdResource(profileId);

        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException | FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }
}