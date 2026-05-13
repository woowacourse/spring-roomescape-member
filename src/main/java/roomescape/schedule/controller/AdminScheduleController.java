package roomescape.schedule.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.schedule.dto.AdminScheduleRequest;
import roomescape.schedule.dto.ScheduleResponse;
import roomescape.schedule.dto.SchedulesResponse;
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
    public ResponseEntity<Void> create(@RequestBody @Valid AdminScheduleRequest request) {
        Long id = scheduleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<SchedulesResponse> findAll() {
        SchedulesResponse responses = scheduleService.findAll();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable @NotNull(message = "스케줄 ID는 필수입니다.") @Positive(message = "스케줄 ID는 양수여야 합니다.") Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
