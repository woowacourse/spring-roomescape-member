package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.dto.TimeSlotRequest;
import roomescape.domain.dto.TimeSlotResponse;
import roomescape.domain.dto.TimeSlotResponses;
import roomescape.service.TimeService;

import java.net.URI;

@RestController
@RequestMapping("/times")
public class TimeController {
    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    public ResponseEntity<TimeSlotResponses> findAll() {
        return ResponseEntity.ok(timeService.findAll());
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
