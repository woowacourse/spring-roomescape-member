package roomescape.reservationtime.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.payload.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getAllTimes() {
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationTimeResponses);
    }

    @GetMapping("/available")
    public ResponseEntity<List<ReservationTimeResponse>> getAvailableReservationTimes(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") Long themeId
    ) {
        List<ReservationTimeResponse> availableReservationTimeResponses =
                reservationTimeService.findAvailableReservationTimes(date, themeId).stream()
                        .map(ReservationTimeResponse::from)
                        .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(availableReservationTimeResponses);
    }

}
