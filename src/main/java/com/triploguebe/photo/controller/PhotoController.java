package com.triploguebe.photo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.triploguebe.photo.dto.*;
import com.triploguebe.photo.service.PhotoService;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping
    public PhotoResponse uploadPhoto(@RequestBody PhotoUploadRequest request) {
        return photoService.uploadPhoto(request);
    }
}
