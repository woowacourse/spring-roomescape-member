package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.ReservationTimeAvailability;
import roomescape.dto.AvailableTimesResponse;
import roomescape.dto.AvailableTimeResponse;
import roomescape.dto.ReservationTimesResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<ReservationTimesResponse> list() {
        return ResponseEntity.ok(ReservationTimesResponse.from(
                reservationTimeService.findAll()
                        .stream()
                        .map(ReservationTimeResponse::from)
                        .toList()));
    }

    @GetMapping("/availability")
    public ResponseEntity<AvailableTimesResponse> getAvailableTimes(@RequestParam("date") LocalDate date,
                                                                    @RequestParam("themeId") Long themeId) {
        List<ReservationTimeAvailability> timeAvailabilities =
                reservationTimeService.findAvailableTimes(date, themeId);

        return ResponseEntity.ok(AvailableTimesResponse.from(timeAvailabilities.stream()
                .map(AvailableTimeResponse::from)
                .toList()));
    }

}
