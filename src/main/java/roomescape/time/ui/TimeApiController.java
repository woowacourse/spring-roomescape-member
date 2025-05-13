package roomescape.time.ui;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.dto.AvailableTimeRequest;
import roomescape.time.dto.AvailableTimeResponse;
import roomescape.time.dto.TimeRequest;
import roomescape.time.dto.TimeResponse;
import roomescape.time.application.TimeService;

@RestController
@RequestMapping("/times")
public class TimeApiController {

    private final TimeService timeService;

    public TimeApiController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping
    public ResponseEntity<TimeResponse> add(@RequestBody TimeRequest timeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(timeService.add(timeRequest));
    }

    @GetMapping
    public ResponseEntity<List<TimeResponse>> findAll() {
        return ResponseEntity.ok(timeService.findAll());
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableTimeResponse>> findByDateAndTheme(
            @ModelAttribute AvailableTimeRequest availableTimeRequest) {
        return ResponseEntity.ok(timeService.findByDateAndThemeId(availableTimeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        timeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
