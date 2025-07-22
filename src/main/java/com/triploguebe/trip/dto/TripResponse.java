package com.triploguebe.trip.dto;

import com.triploguebe.location.dto.LocationResponse;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripResponse {
    private Long triplogId;
    private String title;
    private String description;
    private LocalDate visitedDate;
    private LocationResponse location;
    private List<String> photos;
}
