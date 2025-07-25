package com.triploguebe.trip.repository;

import com.triploguebe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.triploguebe.trip.entity.TripLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TripLogRepository extends JpaRepository<TripLog, Long> {
    List<TripLog> findByUser(User user);

    @Query("SELECT t FROM TripLog t JOIN FETCH t.user WHERE t.id = :triplogId")
    Optional<TripLog> findByIdWithUser(@Param("triplogId") Long triplogId);
}
