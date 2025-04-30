package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.entity.ReservationTime;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> getAllReservationTime() {
        List<ReservationTimeResponse> allReservationTime = reservationTimeService.getAllReservationTime();
        return ResponseEntity.ok(allReservationTime);
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> createReservationTime(
            @RequestBody @Valid ReservationTimeRequest reservationTimeRequest) {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.createReservationTime(
                reservationTimeRequest);
        // TODO : 더 정확한 리소스 위치 표기 필요
        return ResponseEntity.created(URI.create("/times")).body(reservationTimeResponse);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /*
    GetMapping : uri ->  /times/possible
    request : date, themeId
    예약 가능한 시간을 가져와야 한다.
    response : List<ReservationTimeResponse>
     */

    @GetMapping("/times/possible")
    public ResponseEntity<List<ReservationTimeResponse>> getAvailableReservationTimes(
            @RequestBody AvailableReservationTimeSearchRequest request
    ) {
        List<ReservationTime> reservationTimes = reservationTimeService.getAvailableReservationTimesOf(request.date(), request.themeId());
        List<ReservationTimeResponse> responses = reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }
}
