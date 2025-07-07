package com.triploguebe.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.triploguebe.trip.entity.TripLocation;

public interface TripLocationRepository extends JpaRepository<TripLocation, Long> {
}
