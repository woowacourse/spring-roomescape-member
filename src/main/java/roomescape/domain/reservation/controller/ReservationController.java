package roomescape.domain.reservation.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservation.dto.BookableTimeResponse;
import roomescape.domain.reservation.dto.BookableTimesRequest;
import roomescape.domain.reservation.service.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/bookable-times")
    public ResponseEntity<List<BookableTimeResponse>> getTimesWithStatus(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") Long themeId) {
        return ResponseEntity.ok(reservationService.findBookableTimes(new BookableTimesRequest(date, themeId)));
    }
}
