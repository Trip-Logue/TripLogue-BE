package com.triploguebe.trip.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripResponse {
    private Long triplogId;
    private String title;
    private String description;
    private LocalDate visitedDate;
    private String placeName;
}
