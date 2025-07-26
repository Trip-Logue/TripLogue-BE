package com.triploguebe.trip.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripSummaryResponse {
    private Long triplogId;
    private String title;
    private LocalDate visitedDate;
    private String thumbnail;
    private String mainLocation;
}
