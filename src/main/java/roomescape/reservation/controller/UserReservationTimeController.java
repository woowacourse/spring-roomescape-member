package roomescape.reservation.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.response.AvailableReservationTimeResponse;
import roomescape.reservation.service.ReservationTimeService;

@RestController
public class UserReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public UserReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableReservationTimeResponse>> getAvailableTimes(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") Long themeId
    ) {
        List<AvailableReservationTimeResponse> responses = reservationTimeService.getAvailableTimes(date, themeId);
        return ResponseEntity.ok().body(responses);
    }
}
