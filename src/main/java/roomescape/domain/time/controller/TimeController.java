package roomescape.domain.time.controller;

import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.time.dto.response.TimeResponseDto;
import roomescape.domain.time.service.TimeService;

@RestController
@RequestMapping("/api")
@Validated
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<TimeResponseDto>> getAvailableTimes(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @RequestParam @Min(1) Long themeId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(timeService.getAvailableTimes(date, themeId, LocalDateTime.now()));
    }
}
