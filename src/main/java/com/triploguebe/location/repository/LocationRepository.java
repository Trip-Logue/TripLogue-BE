package com.triploguebe.location.repository;

import com.triploguebe.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByLocationNameContaining(String locationName);
    List<Location> findByAddressContaining(String address);

    //위치 이름과 주소로 찾기
    @Query("SELECT l FROM Location l WHERE l.locationName LIKE %:keyword% OR l.address LIKE %:keyword%")
    List<Location> findByKeyword(@Param("keyword") String keyword);

    //위도, 경도 기준으로 찾기
    @Query("SELECT l FROM Location l WHERE " +
            "l.latitude BETWEEN :minLat AND :maxLat AND " +
            "l.longitude BETWEEN :minLng AND :maxLng")
    List<Location> findByCoordinateRange(
            @Param("minLat") Double minLatitude,
            @Param("maxLat") Double maxLatitude,
            @Param("minLng") Double minLongitude,
            @Param("maxLng") Double maxLongitude
    );
}

