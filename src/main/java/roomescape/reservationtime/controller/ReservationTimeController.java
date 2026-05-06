package roomescape.reservationtime.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;

@RequiredArgsConstructor
@RequestMapping("/times")
@RestController
public class ReservationTimeController {

    private final ReservationTimeService timeService;

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findAvailableTimes(
            @RequestParam Long themeId,
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(timeService.findAvailableTimes(themeId, date));
    }
}
