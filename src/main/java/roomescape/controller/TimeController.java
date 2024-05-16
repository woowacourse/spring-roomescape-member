package roomescape.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import roomescape.dto.request.TimeCreateRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.TimeResponse;
import roomescape.service.TimeService;

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
        return ResponseEntity.created(location)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        service.deleteTime(id);
        return ResponseEntity.noContent()
                .build();
    }
}

