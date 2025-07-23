package com.triploguebe.photo.repository;

import com.triploguebe.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    //특정 TripLog의 모든 사진 조회
    List<Photo> findAllByTripLogId(Long tripLogId);

    //특정 날짜의 사진들 조회
    List<Photo> findByUploadedDate(LocalDate date);

    //특정 TripLog의 사진 개수 세기
    int countByTripLogId(Long tripLogId);
}

