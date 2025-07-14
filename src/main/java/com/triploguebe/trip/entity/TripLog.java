package com.triploguebe.trip.entity;

import com.triploguebe.location.entity.Location;
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

    @Column(updatable = false)
    private LocalDate createdDate;
    private LocalDate visitedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDate.now(); // createdDate 자동 세팅
    }
}
