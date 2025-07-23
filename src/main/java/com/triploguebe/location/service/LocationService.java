package com.triploguebe.location.service;

import com.triploguebe.global.exception.CustomException;
import com.triploguebe.global.exception.ErrorCode;
import com.triploguebe.location.dto.LocationResponse;
import com.triploguebe.location.entity.Location;
import com.triploguebe.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationResponse> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(LocationResponse::from)
                .collect(Collectors.toList());
    }

    public LocationResponse getLocationById(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));

        return LocationResponse.from(location);
    }

    public List<LocationResponse> searchLocationsByName(String locationName) {
        return locationRepository.findByLocationNameContaining(locationName)
                .stream()
                .map(LocationResponse::from)
                .collect(Collectors.toList());
    }

    public List<LocationResponse> searchLocationsByKeyword(String keyword) {
        return locationRepository.findByKeyword(keyword)
                .stream()
                .map(LocationResponse::from)
                .collect(Collectors.toList());
    }

    public List<LocationResponse> getLocationsByCoordinateRange(
            Double minLatitude, Double maxLatitude,
            Double minLongitude, Double maxLongitude) {

        return locationRepository.findByCoordinateRange(
                        minLatitude, maxLatitude, minLongitude, maxLongitude)
                .stream()
                .map(LocationResponse::from)
                .collect(Collectors.toList());
    }

    public Location findLocationById(Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_LOCATION));
    }

    public boolean existsById(Long locationId) {
        return locationRepository.existsById(locationId);
    }
}

