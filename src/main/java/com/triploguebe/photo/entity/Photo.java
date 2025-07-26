package com.triploguebe.photo.entity;

import com.triploguebe.trip.entity.TripLog;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    private String imageUrl;
    private LocalDate uploadedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "triplog_id")
    private TripLog tripLog;
}


