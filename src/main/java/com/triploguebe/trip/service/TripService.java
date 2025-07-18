package com.triploguebe.trip.service;

import com.triploguebe.global.exception.CustomException;
import com.triploguebe.global.exception.ErrorCode;
import com.triploguebe.location.dto.LocationResponse;
import com.triploguebe.location.entity.Location;
import com.triploguebe.location.service.LocationService;
import com.triploguebe.photo.entity.Photo;
import com.triploguebe.trip.entity.TripLog;
import com.triploguebe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.triploguebe.trip.dto.*;
import com.triploguebe.trip.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripLogRepository tripLogRepository;
    private final LocationService locationService;

    public Long createTrip(TripCreateRequest request, User user) {
        Location location = locationService.findLocationById(request.getLocationId());

        TripLog tripLog = TripLog.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .visitedDate(request.getVisitedDate())
                .location(location)
                .user(user)
                .build();

        tripLogRepository.save(tripLog);
        return tripLog.getTriplogId();
    }

    public void updateTrip(Long triplogId, TripUpdateRequest request, User user) {
        TripLog tripLog = getTripLog(triplogId);
        validateUser(tripLog, user);

        Location location = locationService.findLocationById(request.getLocationId());

        tripLog.update(request.getTitle(), request.getDescription(), request.getVisitedDate(), location);
    }

    public void deleteTrip(Long triplogId, User user) {
        TripLog tripLog = getTripLog(triplogId);
        validateUser(tripLog, user);

        tripLogRepository.delete(tripLog);
    }

    public TripResponse getTripDetail(Long triplogId) {
        TripLog tripLog = getTripLog(triplogId);

        return new TripResponse(
                tripLog.getTriplogId(),
                tripLog.getTitle(),
                tripLog.getDescription(),
                tripLog.getVisitedDate(),
                LocationResponse.from(tripLog.getLocation()),
                tripLog.getPhotos().stream()
                        .map(Photo::getImageUrl)
                        .collect(Collectors.toList())
        );
    }

    public List<TripResponse> getAllTrips(User user) {
        return tripLogRepository.findByUser(user).stream()
                .map(trip -> new TripResponse(
                        trip.getTriplogId(),
                        trip.getTitle(),
                        trip.getDescription(),
                        trip.getVisitedDate(),
                        LocationResponse.from(trip.getLocation()),
                        trip.getPhotos().stream()
                                .map(Photo::getImageUrl)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    private TripLog getTripLog(Long triplogId) {
        return tripLogRepository.findById(triplogId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRIP_NOT_FOUND));
    }

    private void validateUser(TripLog tripLog, User user) {
        if (!tripLog.getUser().equals(user)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}

