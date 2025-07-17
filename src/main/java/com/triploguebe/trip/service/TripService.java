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

    // 새로운 여행 기록 생성
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

        // 수정할 Location 새로 불러옴
        Location location = locationService.findLocationById(request.getLocationId());

        // TripUpdateRequest로 받은 수정 내용을 TripLog에 반영
        tripLog.update(request.getTitle(), request.getDescription(), request.getVisitedDate(), location);
    }

    public void deleteTrip(Long triplogId, User user) {
        TripLog tripLog = getTripLog(triplogId);
        validateUser(tripLog, user);

        tripLogRepository.delete(tripLog);
    }

    // 특정 여행 기록 조회
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

    // 로그인한 사용자 기준 본인의 기록만 전체 조회
    // 리스트 형태로 TripResponse 변환
    public List<TripResponse> getAllTrips(User user) {
        // 특정 사용자의 여행 기록만 가져오기 위함
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

    // 작성자와 로그인 사용자 일치 여부 검사
    private void validateUser(TripLog tripLog, User user) {
        if (!tripLog.getUser().equals(user)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}

