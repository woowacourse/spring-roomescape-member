package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.ReservationTime;
import roomescape.dto.TimeCreateRequest;
import roomescape.service.TimeService;

import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {
    private final TimeService service;

    public TimeController(TimeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTime>> readTimes() {
        return ResponseEntity.ok(service.readTimes());
    }

    @PostMapping
    public ResponseEntity<ReservationTime> createTime(@RequestBody TimeCreateRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createTime(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable long id) {
        service.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}

