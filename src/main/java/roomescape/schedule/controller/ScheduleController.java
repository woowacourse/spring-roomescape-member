package roomescape.schedule.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.schedule.dto.ScheduleRequest;
import roomescape.schedule.dto.SchedulesResponse;
import roomescape.schedule.service.ScheduleService;

import java.time.LocalDate;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<SchedulesResponse> findAvailableSchedules(
            @RequestParam @NotNull(message = "예약 날짜는 필수입니다.") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam @NotNull(message = "테마 ID는 필수입니다.") @Positive(message = "테마 ID는 양수여야 합니다.") Long themeId) {
        SchedulesResponse responses = scheduleService.findAvailableSchedules(new ScheduleRequest(date, themeId));
        return ResponseEntity.ok(responses);
    }
}
