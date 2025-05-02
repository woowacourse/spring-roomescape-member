package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.response.AvailableTimeResponse;
import roomescape.reservation.service.TimeService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class UserTimeController {

    private final TimeService timeService;

    public UserTimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTimeResponse>> getAvailableTimes(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") Long themeId
    ) {
        List<AvailableTimeResponse> responses = timeService.getAvailableTimes(date, themeId);
        return ResponseEntity.ok().body(responses);
    }
}
