package com.triploguebe.photo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoUploadRequest {
    private String imageUrl;
    private Long triplogId;
}
