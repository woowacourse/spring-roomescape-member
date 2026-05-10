package roomescape.user.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.dto.AvailableTimeResponse;
import roomescape.user.dto.ReservationTimeResponse;
import roomescape.user.dto.ReservationTimesResponse;
import roomescape.user.service.ReservationTimeService;

@RestController
@RequestMapping("/user")
public class ReservationTimeController {

    private final ReservationTimeService timeService;

    public ReservationTimeController(ReservationTimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/times")
    public ResponseEntity<ReservationTimesResponse> readAll() {
        List<ReservationTimeResponse> reservationTimes = timeService.findAll().stream()
                .map(ReservationTimeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new ReservationTimesResponse(reservationTimes, reservationTimes.size()));
    }

    @GetMapping(value = "/times", params = {"themeId", "date"})
    public ResponseEntity<List<AvailableTimeResponse>> readByThemeIdAndDate(@RequestParam Long themeId,
                                                                            @RequestParam LocalDate date) {
        List<AvailableTimeResponse> availableTimes = timeService.findByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(availableTimes);
    }
}
