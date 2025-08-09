package com.triploguebe.trip.controller;

import com.triploguebe.global.exception.CustomException;
import com.triploguebe.global.exception.ErrorCode;
import com.triploguebe.user.entity.User;
import com.triploguebe.user.repository.UserRepository;
import com.triploguebe.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserService userService;

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripCreateRequest request,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername()); // 유저 엔티티 조회
        TripCreateResponse response = tripService.createTrip(request, user);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{triplogId}")
    public ResponseEntity<TripCreateResponse> updateTrip(@PathVariable Long triplogId, @RequestBody @Valid TripUpdateRequest request, @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername());
        TripCreateResponse response = tripService.updateTrip(triplogId, request, user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{triplogId}")
    public ResponseEntity<?> deleteTrip(@PathVariable Long triplogId, @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername());
        tripService.deleteTrip(triplogId, user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "여행 기록이 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{triplogId}")
    public ResponseEntity<TripResponse> getTripDetail(@PathVariable Long triplogId) {

        TripResponse response = tripService.getTripDetail(triplogId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TripSummaryResponse>> getAllTrips(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername()); // user 엔티티 조회
        List<TripSummaryResponse> response = tripService.getAllTrips(user);
        return ResponseEntity.ok(response);
    }
}
