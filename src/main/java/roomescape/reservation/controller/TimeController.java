package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.dto.request.TimeRequest;
import roomescape.reservation.dto.response.TimeResponse;
import roomescape.reservation.service.TimeService;

import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping
    public ResponseEntity<TimeResponse> createTime(@RequestBody TimeRequest request) {
        TimeResponse response = timeService.create(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<List<TimeResponse>> getAllTimes() {
        List<TimeResponse> responses = timeService.getAllTimes();
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        timeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
