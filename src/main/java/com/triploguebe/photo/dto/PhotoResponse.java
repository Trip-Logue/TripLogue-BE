package com.triploguebe.photo.dto;

import com.triploguebe.photo.entity.Photo;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoResponse {
    private Long photoId;
    private String imageUrl;
    private LocalDate uploadedDate;

    public static PhotoResponse from(Photo photo) {
        return PhotoResponse.builder()
                .photoId(photo.getPhotoId())
                .imageUrl(photo.getImageUrl())
                .uploadedDate(photo.getUploadedDate())
                .build();
    }
}
