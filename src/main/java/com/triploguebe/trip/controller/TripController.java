package com.triploguebe.trip.controller;

import com.triploguebe.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.triploguebe.trip.service.TripService;
import com.triploguebe.trip.dto.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/triplog")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody @Valid TripCreateRequest request, @AuthenticationPrincipal User user) {

        TripCreateResponse response = tripService.createTrip(request, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{triplogId}")
    public ResponseEntity<TripCreateResponse> updateTrip(@PathVariable Long triplogId, @RequestBody @Valid TripUpdateRequest request, @AuthenticationPrincipal User user) {

        TripCreateResponse response = tripService.updateTrip(triplogId, request, user);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{triplogId}")
    public ResponseEntity<?> deleteTrip(@PathVariable Long triplogId, @AuthenticationPrincipal User user) {

        tripService.deleteTrip(triplogId, user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "여행 기록이 삭제되었습니다.");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{triplogId}")
    public ResponseEntity<TripResponse> getTripDetail(@PathVariable Long triplogId) {

        TripResponse response = tripService.getTripDetail(triplogId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TripSummaryResponse>> getAllTrips(@AuthenticationPrincipal User user) {

        List<TripSummaryResponse> response = tripService.getAllTrips(user);

        return ResponseEntity.ok(response);
    }
}
