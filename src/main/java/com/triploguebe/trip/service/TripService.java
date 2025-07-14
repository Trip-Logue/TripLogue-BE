package com.triploguebe.trip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.triploguebe.trip.dto.*;
import com.triploguebe.trip.repository.*;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripLogRepository tripLogRepository;

    public TripResponse createTrip(TripCreateRequest request) {
        return null;
    }
}
