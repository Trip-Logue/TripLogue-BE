package com.triploguebe.photo.controller;

import com.triploguebe.photo.dto.PhotoResponse;
import com.triploguebe.photo.dto.PhotoUpdateRequest;
import com.triploguebe.photo.dto.PhotoUploadRequest;
import com.triploguebe.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    //사진 업로드
    @PostMapping
    public ResponseEntity<PhotoResponse> uploadPhoto(@RequestBody PhotoUploadRequest request) {
        PhotoResponse response = photoService.uploadPhoto(request);
        return ResponseEntity.ok(response);
    }

    //사진 삭제
    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable("photoId") Long photoId) {
        photoService.deletePhoto(photoId);
        return ResponseEntity.noContent().build();
    }

    //모든 사진 조회
    @GetMapping
    public ResponseEntity<List<PhotoResponse>> getAllPhotos() {
        List<PhotoResponse> photos = photoService.getAllPhotos();
        return ResponseEntity.ok(photos);
    }

    //특정 사진 조회
    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoResponse> getPhoto(@PathVariable("photoId") Long photoId) {
        PhotoResponse photo = photoService.getPhotoById(photoId);
        return ResponseEntity.ok(photo);
    }

    //사진 정보 수정
    @PutMapping("/{photoId}")
    public ResponseEntity<PhotoResponse> updatePhoto(
            @PathVariable("photoId") Long photoId,
            @RequestBody PhotoUpdateRequest request) {
        PhotoResponse response = photoService.updatePhoto(photoId, request);
        return ResponseEntity.ok(response);
    }
}


