package com.triploguebe.photo.service;

import com.triploguebe.global.exception.CustomException;
import com.triploguebe.global.exception.ErrorCode;
import com.triploguebe.photo.dto.PhotoResponse;
import com.triploguebe.photo.dto.PhotoUpdateRequest;
import com.triploguebe.photo.entity.Photo;
import com.triploguebe.photo.repository.PhotoRepository;
import com.triploguebe.trip.entity.TripLog;
import com.triploguebe.trip.repository.TripLogRepository;
import com.triploguebe.global.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final TripLogRepository tripLogRepository;
    private final AwsS3Service awsS3Service;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Transactional
    public PhotoResponse uploadPhoto(MultipartFile file, Long triplogId) {
        TripLog tripLog = tripLogRepository.findById(triplogId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRIP_NOT_FOUND));

        String fileName = "photo-" + System.currentTimeMillis();
        String path = "photos/" + triplogId + "/";

        String s3Key = awsS3Service.uploadFile(file, path, fileName);

        String imageUrl = awsS3Service.getFileUrl(s3Key);

        Photo photo = Photo.builder()
                .tripLog(tripLog)
                .imageUrl(imageUrl)
                .uploadedDate(LocalDate.now())
                .build();

        Photo savedPhoto = photoRepository.save(photo);

        return PhotoResponse.from(savedPhoto);
    }

    public List<PhotoResponse> getAllPhotos() {
        return photoRepository.findAll()
                .stream()
                .map(PhotoResponse::from)
                .collect(Collectors.toList());
    }

    public PhotoResponse getPhotoById(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));
        return PhotoResponse.from(photo);
    }

    @Transactional
    public void deletePhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));

        String imageUrl = photo.getImageUrl();
        String bucketUrlPrefix = "https://" + bucketName + ".s3." + region + ".amazonaws.com/";

        if (imageUrl.startsWith(bucketUrlPrefix)) {
            String s3Key = imageUrl.substring(bucketUrlPrefix.length());
            awsS3Service.deleteFile(s3Key);
        }

        photoRepository.delete(photo);
    }

    @Transactional
    public PhotoResponse updatePhoto(Long photoId, MultipartFile file, PhotoUpdateRequest request) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));

        if (file != null && !file.isEmpty()) {
            String oldImageUrl = photo.getImageUrl();
            String bucketUrlPrefix = "https://" + bucketName + ".s3." + region + ".amazonaws.com/";

            if (oldImageUrl.startsWith(bucketUrlPrefix)) {
                String oldS3Key = oldImageUrl.substring(bucketUrlPrefix.length());
                awsS3Service.deleteFile(oldS3Key);
            }

            String path = "photos/" + photo.getTripLog().getId() + "/";
            String fileName = "photo-" + System.currentTimeMillis();

            String newS3Key = awsS3Service.uploadFile(file, path, fileName);
            String newImageUrl = awsS3Service.getFileUrl(newS3Key);

            photo.setImageUrl(newImageUrl);
        } else if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            photo.setImageUrl(request.getImageUrl());
        }

        Photo updatedPhoto = photoRepository.save(photo);
        return PhotoResponse.from(updatedPhoto);
    }
}
