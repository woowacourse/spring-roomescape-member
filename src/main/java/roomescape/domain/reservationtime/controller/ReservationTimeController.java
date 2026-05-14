package roomescape.domain.reservationtime.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservationtime.response.ReservationTimesResponse;
import roomescape.domain.reservationtime.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<ReservationTimesResponse> findAll() {
        ReservationTimesResponse times = reservationTimeService.findAllReservationTimes();
        return ResponseEntity.ok(times);
    }
}
