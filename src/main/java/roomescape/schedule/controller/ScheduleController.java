package roomescape.schedule.controller;

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
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam Long themeId) {
        SchedulesResponse responses = scheduleService.findAvailableSchedules(new ScheduleRequest(date, themeId));
        return ResponseEntity.ok(responses);
    }
}
