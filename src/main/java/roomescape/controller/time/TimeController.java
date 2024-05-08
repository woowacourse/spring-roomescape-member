package roomescape.controller.time;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.service.TimeService;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    public TimeController(final TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    public ResponseEntity<List<TimeResponse>> getTimes(
            @RequestParam(value = "date", required = false) final String date,
            @RequestParam(value = "themeId", required = false) final Long themeId) {
        if (Objects.isNull(date) || Objects.isNull(themeId)) {
            return ResponseEntity.ok(timeService.getTimes());
        }
        return ResponseEntity.ok(timeService.getTimesWithBooked(date, themeId));
    }

    @PostMapping
    public ResponseEntity<TimeResponse> addTime(@RequestBody final TimeRequest timeRequest) {
        final TimeResponse time = timeService.addTime(timeRequest);
        final URI uri = UriComponentsBuilder.fromPath("/times/{id}")
                .buildAndExpand(time.id())
                .toUri();

        return ResponseEntity.created(uri).body(time);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable final Long id) {
        timeService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}
