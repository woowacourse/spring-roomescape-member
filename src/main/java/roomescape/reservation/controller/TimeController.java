package roomescape.reservation.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.request.TimeRequest;
import roomescape.reservation.dto.response.TimeResponse;
import roomescape.reservation.service.TimeService;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping
    public ResponseEntity<TimeResponse> createTime(@RequestBody TimeRequest request) {
        TimeResponse response = timeService.createTime(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<List<TimeResponse>> getAllTimes() {
        List<TimeResponse> responses = timeService.getAllTimes();
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        timeService.deleteTime(id);
        return ResponseEntity.ok().build();
    }
}
