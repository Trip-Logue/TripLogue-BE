package com.triploguebe.trip.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripUpdateRequest {
    private String title;
    private String description;
}

