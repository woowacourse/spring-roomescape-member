package roomescape.domain.reservation.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservation.response.ReservationTimeResponse;
import roomescape.domain.reservation.response.ReservationTimesResponse;
import roomescape.domain.reservation.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<ReservationTimesResponse> findAll() {
        List<ReservationTimeResponse> times = reservationTimeService.findAllReservationTimes();
        return ResponseEntity.ok(new ReservationTimesResponse(times));
    }
}
