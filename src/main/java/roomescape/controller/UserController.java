package roomescape.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeQueryService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final ReservationTimeQueryService reservationTimeQueryService;

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> getAvailableTimes(
            @RequestParam LocalDate date,
            @RequestParam long themeId) {
        return ResponseEntity.ok(reservationTimeQueryService.findAvailableReservationTimes(date, themeId));
    }
}
