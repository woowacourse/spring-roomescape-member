package roomescape.domain.time.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.time.dto.response.TimeResponseDTO;
import roomescape.domain.time.service.TimeService;

@RestController
@RequestMapping("/api/times")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    public ResponseEntity<List<TimeResponseDTO>> getAvailableTimes(@RequestParam LocalDate date,
        @RequestParam Long themeId) {
        return ResponseEntity.ok(timeService.getAvailableTimes(date, themeId));
    }
}
