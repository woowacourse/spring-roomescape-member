package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.reservationTime.ReservationTimeResponse;
import roomescape.dto.reservationTime.ReservationTimeResponses;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<ReservationTimeResponses> readTimes() {
        return ResponseEntity.ok(ReservationTimeResponses.from(reservationTimeService.getReservationTimes()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationTimeResponse> readTime(@PathVariable Long id) {
        return ResponseEntity.ok(ReservationTimeResponse.from(reservationTimeService.getReservationTime(id)));
    }
}
