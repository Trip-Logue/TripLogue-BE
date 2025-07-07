package com.triploguebe.trip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripLocation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    private double latitude;
    private double longitude;
    private String address;
    private String placeName;
}

