package com.triploguebe.trip.controller;

import com.triploguebe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.triploguebe.trip.service.TripService;
import com.triploguebe.trip.dto.*;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public Long createTrip(@RequestBody TripCreateRequest request, @AuthenticationPrincipal User user) {
        return tripService.createTrip(request, user);
    }
}
