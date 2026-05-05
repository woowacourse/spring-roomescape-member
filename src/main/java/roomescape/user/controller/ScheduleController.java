package roomescape.user.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.dto.ScheduleResponse;
import roomescape.user.service.ScheduleService;

@RestController
@RequestMapping("/user")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleResponse>> readById(@RequestParam("themeId") Long themeId,
                                                           @RequestParam("date") LocalDate date) {
        List<ScheduleResponse> schedules = scheduleService.findById(themeId, date).stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(schedules);
    }
}
