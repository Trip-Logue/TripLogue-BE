package com.triploguebe.photo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.triploguebe.photo.dto.PhotoUploadRequest;
import com.triploguebe.photo.dto.PhotoResponse;
import com.triploguebe.photo.repository.PhotoRepository;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

    public PhotoResponse uploadPhoto(PhotoUploadRequest request) {
        return null;
    }
}

