package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.TimeSlotRequest;
import roomescape.dto.response.TimeSlotResponse;
import roomescape.service.TimeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {
    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    public ResponseEntity<List<TimeSlotResponse>> findAll() {
        List<TimeSlotResponse> timeSlots = timeService.findAll();
        return ResponseEntity.ok(timeSlots);
    }

    @PostMapping
    public ResponseEntity<TimeSlotResponse> create(@RequestBody TimeSlotRequest timeSlotRequest) {
        TimeSlotResponse timeSlotResponse = timeService.create(timeSlotRequest);
        return ResponseEntity.created(URI.create("/times/" + timeSlotResponse.id())).body(timeSlotResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
