package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.AvailableTimeResponse;
import roomescape.dto.ErrorResponse;
import roomescape.dto.TimeCreateRequest;
import roomescape.dto.TimeResponse;
import roomescape.service.TimeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {
    private final TimeService service;

    public TimeController(TimeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TimeResponse>> readTimes() {
        List<TimeResponse> response = service.readTimes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableTimeResponse>> readAvailableTimes(@RequestParam String date, @RequestParam Long themeId) {
        List<AvailableTimeResponse> response = service.readAvailableTimes(date, themeId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TimeResponse> createTime(@RequestBody TimeCreateRequest dto) {
        TimeResponse response = service.createTime(dto);

        URI location = URI.create("/times/" + response.id());
        return ResponseEntity
                .created(location)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        service.deleteTime(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}

