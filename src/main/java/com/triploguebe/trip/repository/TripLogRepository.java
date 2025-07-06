package com.triploguebe.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.triploguebe.trip.entity.TripLog;

public interface TripLogRepository extends JpaRepository<TripLog, Long> {
}
