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
        try {
            Resource resource = storageService.loadAsResource(Paths.get(path).getFileName().toString());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"").body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException | StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/get_image_id")
    public ResponseEntity<?> getImageById(@RequestParam Long id) {
        try {
            Resource resource = storageService.getResource(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException | FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> applyFilterByImageId(@RequestParam Long id,
                                                  @RequestParam Filters filter) {
        try {
            Resource resource = storageService.getFilteredImage(id, filter);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException | StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/frame")
    public ResponseEntity<?> addFrameByImageId(@RequestParam Long id,
                                               @RequestParam Long frameId) {
        try {
            Resource resource = storageService.getImageWithFrame(id, frameId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException | StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/get_preview_img_id")
    public ResponseEntity<?> getPreviewByImageId(@RequestParam Long id) {
        try {
            Resource resource = storageService.getPreview(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException | StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/get_collage")
    public ResponseEntity<?> getCollage(@RequestParam("ids") List<Long> ids) {
        if (ids.isEmpty() || ids.size() > 4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ActionMessage("There's no id to make a collage" +
                            " or you're trying to send to much photos (>4)"));
        }

        try {
            Resource resource = storageService.getCollage(ids);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException | StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @DeleteMapping("/delete_image")
    public ResponseEntity<ActionMessage> deleteImageById(@RequestParam Long id) {
        try {
            storageService.deleteImage(id);

            return ResponseEntity.ok()
                    .body(new ActionMessage("The image was deleted"));
        } catch (IOException | com.amazonaws.SdkClientException | FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @DeleteMapping("/frame")
    public ResponseEntity<ActionMessage> deleteFrameById(@RequestParam Long id) {
        try {
            storageService.deleteFrame(id);

            return ResponseEntity.ok()
                    .body(new ActionMessage("The frame was deleted"));
        } catch (FileNotFoundException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @PutMapping("/edit_info")
    public ResponseEntity<ActionMessage> editImageInfo(@RequestParam Long imageId,
                                                       @RequestParam String newName) {
        try {
            storageService.editInfo(imageId, new String(newName.getBytes(), StandardCharsets.UTF_8));

            return ResponseEntity.ok()
                    .body(new ActionMessage("Image information has been successfully edited"));
        } catch (StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @PostMapping("/upload_image")
    public ResponseEntity<?> uploadImage(@RequestParam("user_id") Long user_id, @RequestParam("space_id") Long space_id,
                                         @RequestParam("file") MultipartFile file) {
        try {
            String name = storageService.store(file);
            String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(name)
                    .toUriString();

            storageService.saveInfo(user_id, space_id, "uploads/" + name);

            return ResponseEntity.ok()
                    .body(new FileResponse(name, uri, file.getContentType(), file.getSize()));
        } catch (IllegalArgumentException | StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
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
        try {
            String name = storageService.store(file);
            String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(name)
                    .toUriString();

            storageService.saveFrameInfo(name);

            return ResponseEntity.ok()
                    .body(new FileResponse(name, uri, file.getContentType(), file.getSize()));
        } catch (IllegalArgumentException | StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }


    @GetMapping("/get_frame_id")
    public ResponseEntity<?> getFrameById(@RequestParam Long id) {
        try {
            Resource resource = storageService.getFrameResource(id);

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
        try {
            Resource resource = storageService.getPreviewOfFrameResource(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException | StorageException e) {
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
        try {
            Resource resource = storageService.getPreviewOfPhotoResource(PhotoPreviewId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException | StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/photo_preview_profile_id")
    public ResponseEntity<?> getPreviewOfPhotoByProfileId(@RequestParam("profile_id") Long profileId) {
        try {
            Resource resource = storageService.getPreviewOfPhotoByProfileResource(profileId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException | StorageException e) {
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
        try {
            String name = storageService.store(file);
            String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(name)
                    .toUriString();

            storageService.savePhotoInfo(id, name);

            return ResponseEntity.ok()
                    .body(new FileResponse(name, uri, file.getContentType(), file.getSize()));
        } catch (IllegalArgumentException | StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }


    @GetMapping("/photo_id")
    public ResponseEntity<?> getPhotoById(@RequestParam Long photoId) {
        try {
            Resource resource = storageService.getPhotoResource(photoId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException | IllegalArgumentException | FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }

    @GetMapping("/photo_profile_id")
    public ResponseEntity<?> getPhotoByProfileId(@RequestParam Long profileId) {
        try {
            Resource resource = storageService.getPhotoByProfileIdResource(profileId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath())));
        } catch (IOException | StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ActionMessage(e.getMessage()));
        }
    }
}