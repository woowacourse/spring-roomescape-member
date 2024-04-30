package roomescape.controller.time;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.service.TimeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    public TimeController(final TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    public List<TimeResponse> getTimes() {
        return timeService.getTimes();
    }

    @PostMapping
    public ResponseEntity<TimeResponse> addTime(@RequestBody final TimeRequest timeRequest) {
        TimeResponse time = timeService.addTime(timeRequest);
        URI uri = UriComponentsBuilder.fromPath("/reservations/{id}")
                .buildAndExpand(time.id())
                .toUri();

        return ResponseEntity.created(uri)
                .body(time);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable("id") final Long id) {
        int deleteCount = timeService.deleteTime(id);
        if (deleteCount == 0) {
            return ResponseEntity.notFound()
                    .build();
        }
        return ResponseEntity.ok()
                .build();
    }
}
