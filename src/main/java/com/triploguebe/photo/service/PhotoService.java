package com.triploguebe.photo.service;

import com.triploguebe.global.exception.CustomException;
import com.triploguebe.global.exception.ErrorCode;
import com.triploguebe.photo.dto.PhotoResponse;
import com.triploguebe.photo.dto.PhotoUpdateRequest;
import com.triploguebe.photo.dto.PhotoUploadRequest;
import com.triploguebe.photo.entity.Photo;
import com.triploguebe.photo.repository.PhotoRepository;
import com.triploguebe.trip.entity.TripLog;
import com.triploguebe.trip.repository.TripLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final TripLogRepository tripLogRepository;

    //사진 업로드
    @Transactional
    public PhotoResponse uploadPhoto(PhotoUploadRequest request) {
        TripLog tripLog = tripLogRepository.findById(request.getTriplogId())
                .orElseThrow(() -> new CustomException(ErrorCode.TRIP_NOT_FOUND));

        Photo photo = Photo.builder()
                .tripLog(tripLog)  // 연관관계 설정
                .imageUrl(request.getImageUrl())
                .uploadedDate(LocalDate.now())
                .build();

        Photo savedPhoto = photoRepository.save(photo);
        return PhotoResponse.from(savedPhoto);
    }

    //특정 TripLog의 모든 사진 조회
    public List<PhotoResponse> getPhotosByTripLogId(Long tripLogId) {
        return photoRepository.findAllByTripLogId(tripLogId)
                .stream()
                .map(PhotoResponse::from)
                .collect(Collectors.toList());
    }

    //특정 사진 조회
    public PhotoResponse getPhotoById(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found with id: " + photoId));
        return PhotoResponse.from(photo);
    }

    //사진 삭제
    @Transactional
    public void deletePhoto(Long photoId) {
        photoRepository.deleteById(photoId);
    }

    //특정 날짜의 사진들 조회
    public List<PhotoResponse> getPhotosByDate(LocalDate date) {
        return photoRepository.findByUploadedDate(date)
                .stream()
                .map(PhotoResponse::from)
                .collect(Collectors.toList());
    }

    //TripLog의 사진 URL 조회
    public List<String> getPhotoUrlsByTripLogId(Long tripLogId) {
        return photoRepository.findAllByTripLogId(tripLogId)
                .stream()
                .map(Photo::getImageUrl)
                .collect(Collectors.toList());
    }

    //사진 정보 수정
    @Transactional
    public PhotoResponse updatePhoto(Long photoId, PhotoUpdateRequest request) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found with id: " + photoId));

        if (request.getImageUrl() != null) {
            photo.setImageUrl(request.getImageUrl());
        }

        Photo updatedPhoto = photoRepository.save(photo);
        return PhotoResponse.from(updatedPhoto);
    }

    //모든 사진 조회
    public List<PhotoResponse> getAllPhotos() {
        return photoRepository.findAll()
                .stream()
                .map(PhotoResponse::from)
                .collect(Collectors.toList());
    }
}


