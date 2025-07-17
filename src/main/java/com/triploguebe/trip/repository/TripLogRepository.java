package com.triploguebe.trip.repository;

import com.triploguebe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.triploguebe.trip.entity.TripLog;

import java.util.List;

public interface TripLogRepository extends JpaRepository<TripLog, Long> {
    List<TripLog> findByUser(User user); // 특정 유저의 모든 여행 기록 가져오는 용도
}
