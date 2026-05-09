package roomescape.schedule.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.schedule.dto.AdminScheduleRequest;
import roomescape.schedule.dto.ScheduleResponse;
import roomescape.schedule.service.ScheduleService;

import java.util.List;

@RestController
@RequestMapping("/admin/schedules")
public class AdminScheduleController {

    private final ScheduleService scheduleService;

    public AdminScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@RequestBody AdminScheduleRequest request) {
        ScheduleResponse response = scheduleService.createByAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findAllSchedules() {
        List<ScheduleResponse> responses = scheduleService.findAllAdmin();
        return ResponseEntity.ok(responses);
    }
}
