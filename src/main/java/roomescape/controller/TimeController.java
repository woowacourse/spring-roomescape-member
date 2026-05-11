package roomescape.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.TimeAllResponse;
import roomescape.dto.TimeRequest;
import roomescape.dto.TimeResponse;
import roomescape.service.TimeService;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    public ResponseEntity<TimeAllResponse> readAll() {
        TimeAllResponse reservationTimes = timeService.readAll();
        return ResponseEntity.ok().body(reservationTimes);
    }

    @GetMapping("/{themeId}")
    public ResponseEntity<TimeAllResponse> readAllByThemeIdAndDate(@PathVariable Long themeId,
                                                                   @RequestParam("date") String date) {
        TimeAllResponse reservationTimes = timeService.readAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(reservationTimes);
    }

    @PostMapping
    public ResponseEntity<TimeResponse> register(@RequestBody TimeRequest timeRequest) {
        TimeResponse timeResponse = timeService.register(timeRequest);
        return ResponseEntity.created(URI.create("/times/" + timeResponse.id())).body(timeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeById(@PathVariable Long id) {
        timeService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
