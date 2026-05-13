package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.dto.reservationtime.ReservationTimeResponses;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<ReservationTimeResponses> readTimes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(reservationTimeService.getReservationTimes(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationTimeResponse> readTime(@PathVariable Long id) {
        return ResponseEntity.ok(ReservationTimeResponse.from(reservationTimeService.getReservationTime(id)));
    }
}
