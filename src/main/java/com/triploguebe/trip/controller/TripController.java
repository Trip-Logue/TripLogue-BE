package com.triploguebe.trip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.triploguebe.trip.service.TripService;
import com.triploguebe.trip.dto.*;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public TripResponse createTrip(@RequestBody TripCreateRequest request) {
        return tripService.createTrip(request);
    }
}
