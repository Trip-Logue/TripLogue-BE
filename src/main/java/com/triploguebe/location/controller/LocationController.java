package com.triploguebe.location.controller;

import com.triploguebe.location.dto.LocationResponse;
import com.triploguebe.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationResponse>> getAllLocations() {
        List<LocationResponse> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationResponse> getLocation(@PathVariable Long locationId) {
        LocationResponse location = locationService.getLocationById(locationId);
        return ResponseEntity.ok(location);
    }

    @GetMapping("/search")
    public ResponseEntity<List<LocationResponse>> searchLocations(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String keyword) {

        List<LocationResponse> locations;

        if (name != null && !name.trim().isEmpty()) {
            locations = locationService.searchLocationsByName(name);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            locations = locationService.searchLocationsByKeyword(keyword);
        } else {
            locations = locationService.getAllLocations();
        }

        return ResponseEntity.ok(locations);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<LocationResponse>> getNearbyLocations(
            @RequestParam Double minLat,
            @RequestParam Double maxLat,
            @RequestParam Double minLng,
            @RequestParam Double maxLng) {

        List<LocationResponse> locations = locationService.getLocationsByCoordinateRange(
                minLat, maxLat, minLng, maxLng);

        return ResponseEntity.ok(locations);
    }
}

