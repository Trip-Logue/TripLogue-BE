package com.triploguebe.trip.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripListResponse {
    private List<TripResponse> trips;
}
