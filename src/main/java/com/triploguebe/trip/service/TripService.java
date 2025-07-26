package com.triploguebe.trip.service;

import com.triploguebe.global.exception.CustomException;
import com.triploguebe.global.exception.ErrorCode;
import com.triploguebe.location.dto.LocationResponse;
import com.triploguebe.location.entity.Location;
import com.triploguebe.location.service.LocationService;
import com.triploguebe.photo.entity.Photo;
import com.triploguebe.trip.entity.TripLog;
import com.triploguebe.user.entity.User;
import com.triploguebe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.triploguebe.trip.dto.*;
import com.triploguebe.trip.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripLogRepository tripLogRepository;
    private final LocationService locationService;
    private final UserRepository userRepository;

    public TripCreateResponse createTrip(TripCreateRequest request, User user) {
        Location location = locationService.findLocationById(request.getLocationId());

        TripLog tripLog = TripLog.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .visitedDate(request.getVisitedDate())
                .location(location)
                .user(user)
                .build();

        TripLog savedTripLog = tripLogRepository.save(tripLog);

        return new TripCreateResponse(savedTripLog.getId(), "여행 기록이 등록되었습니다.");
    }

    @Transactional
    public TripCreateResponse updateTrip(Long triplogId, TripUpdateRequest request, User user) {
        TripLog tripLog = getTripLog(triplogId);
        validateUser(user,tripLog);

        Location location = locationService.findLocationById(request.getLocationId());

        tripLog.update(request.getTitle(), request.getDescription(), request.getVisitedDate(), location);

        return new TripCreateResponse(triplogId, "여행 정보가 수정되었습니다.");
    }

    @Transactional
    public void deleteTrip(Long triplogId, User user) {
        TripLog tripLog = getTripLog(triplogId);
        validateUser(user, tripLog);

        tripLogRepository.delete(tripLog);
    }

    public TripResponse getTripDetail(Long triplogId) {
        TripLog tripLog = getTripLog(triplogId);

        return new TripResponse(
                tripLog.getId(),
                tripLog.getTitle(),
                tripLog.getDescription(),
                tripLog.getVisitedDate(),
                LocationResponse.from(tripLog.getLocation()),
                tripLog.getPhotos().stream()
                        .map(Photo::getImageUrl)
                        .collect(Collectors.toList())
        );
    }

    public List<TripSummaryResponse> getAllTrips(User user) {
        return tripLogRepository.findByUser(user).stream()
                .map(trip -> new TripSummaryResponse(
                        trip.getId(),
                        trip.getTitle(),
                        trip.getVisitedDate(),
                        trip.getPhotos().stream()
                                .findFirst()
                                .map(Photo::getImageUrl)
                                .orElse(null),
                        trip.getLocation() != null ? trip.getLocation().getLocationName() : null
                ))
                .collect(Collectors.toList());
    }

    private TripLog getTripLog(Long triplogId) {
        return tripLogRepository.findByIdWithUser(triplogId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRIP_NOT_FOUND));
    }

    private void validateUser(User user, TripLog tripLog) {
        if (tripLog.getUser() == null || !Objects.equals(tripLog.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}

