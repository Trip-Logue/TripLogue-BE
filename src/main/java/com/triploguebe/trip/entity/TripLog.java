package com.triploguebe.trip.entity;

import com.triploguebe.location.entity.Location;
import com.triploguebe.photo.entity.Photo;
import com.triploguebe.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "triplog_id")
    private Long id;

    private String title;
    private String description;

    @Column(updatable = false)
    private LocalDate createdDate;
    private LocalDate visitedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "tripLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDate.now();
    }

    public void update(String title, String description, LocalDate date, Location location) {
        this.title = title;
        this.description = description;
        this.visitedDate = date;
        this.location = location;
    }
}