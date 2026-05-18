package roomescape.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.reservationtime.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/times")
@RestController
public class UserReservationTimeRestController {

    private final ReservationTimeService reservationTimeService;

    public UserReservationTimeRestController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> read(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long themeId
            ) {
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.read(date, themeId);
        return ResponseEntity.ok(reservationTimes);
    }
}
