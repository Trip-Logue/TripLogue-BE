package com.triploguebe.trip.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripUpdateRequest {
    private String title;
    private String description;
    private LocalDate visitedDate;
    private Long locationId;
}

