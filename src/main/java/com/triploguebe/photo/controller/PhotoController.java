package com.triploguebe.photo.controller;

import com.triploguebe.photo.dto.PhotoResponse;
import com.triploguebe.photo.dto.PhotoUpdateRequest;
import com.triploguebe.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping
    public ResponseEntity<PhotoResponse> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("triplogId") Long triplogId
    ) {
        PhotoResponse response = photoService.uploadPhoto(file, triplogId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId) {
        photoService.deletePhoto(photoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PhotoResponse>> getAllPhotos() {
        List<PhotoResponse> photos = photoService.getAllPhotos();
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoResponse> getPhoto(@PathVariable Long photoId) {
        PhotoResponse photo = photoService.getPhotoById(photoId);
        return ResponseEntity.ok(photo);
    }

    @PutMapping("/{photoId}")
    public ResponseEntity<PhotoResponse> updatePhoto(
            @PathVariable Long photoId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestBody PhotoUpdateRequest request
    ) {
        PhotoResponse response = photoService.updatePhoto(photoId, file, request);
        return ResponseEntity.ok(response);
    }
}
