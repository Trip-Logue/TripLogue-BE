package com.triploguebe.location.dto;

import com.triploguebe.location.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationResponse {

    private Long locationId;
    private String locationName;
    private String address;
    private Double latitude;
    private Double longitude;
    private String description;

    public static LocationResponse from(Location location) {
        return LocationResponse.builder()
                .locationId(location.getLocationId())
                .locationName(location.getLocationName())
                .address(location.getAddress())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .description(location.getDescription())
                .build();
    }
}

