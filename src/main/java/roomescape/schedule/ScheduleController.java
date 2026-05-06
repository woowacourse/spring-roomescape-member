package roomescape.schedule;

import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<ScheduleResponse> getSchedules(@RequestParam LocalDate date,
                                                         @RequestParam Long themeId) {
        ScheduleResponse response = scheduleService.getSchedules(date, themeId);
        return ResponseEntity.ok(response);
    }
}
