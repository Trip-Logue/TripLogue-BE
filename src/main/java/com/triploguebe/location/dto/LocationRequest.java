package com.triploguebe.location.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationRequest {
    private String locationName;
    private String address;
    private double latitude;
    private double longitude;
    private String description;
}
