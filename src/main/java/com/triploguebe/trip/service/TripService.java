package com.triploguebe.trip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.triploguebe.trip.dto.*;
import com.triploguebe.trip.entity.*;
import com.triploguebe.trip.repository.*;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripLogRepository tripLogRepository;
    private final TripLocationRepository tripLocationRepository;

    public TripResponse createTrip(TripCreateRequest request) {
        return null;
    }
}
