package com.triploguebe.trip.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long triplogId;

    private String title;
    private String description;
    private LocalDate createdDate;
    private LocalDate visitedDate;
}
