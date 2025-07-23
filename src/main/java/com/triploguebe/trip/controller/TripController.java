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

}
