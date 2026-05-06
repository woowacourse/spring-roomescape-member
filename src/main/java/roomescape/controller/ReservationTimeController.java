package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.ReservationTimeAvailability;
import roomescape.controller.dto.AvailableTimeListResponse;
import roomescape.controller.dto.AvailableTimeResponse;
import roomescape.controller.dto.ReservationTimeListResponse;
import roomescape.controller.dto.ReservationTimeResponse;
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
    public ResponseEntity<ReservationTimeListResponse> list() {
        return ResponseEntity.ok(ReservationTimeListResponse.from(
                reservationTimeService.findAll()
                        .stream()
                        .map(ReservationTimeResponse::from)
                        .toList()));
    }

    @GetMapping("/availability")
    public ResponseEntity<AvailableTimeListResponse> getAvailableTimes(@RequestParam("date") LocalDate date,
                                                                       @RequestParam("themeId") Long themeId) {
        List<ReservationTimeAvailability> timeAvailabilities =
                reservationTimeService.findAvailableTimes(date, themeId);

        return ResponseEntity.ok(AvailableTimeListResponse.from(timeAvailabilities.stream()
                .map(AvailableTimeResponse::from)
                .toList()));
    }

}
