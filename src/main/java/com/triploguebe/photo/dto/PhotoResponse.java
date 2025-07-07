package com.triploguebe.photo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoResponse {
    private Long photoId;
    private String imageUrl;
}