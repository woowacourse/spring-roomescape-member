package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.TimeRequest;
import roomescape.controller.dto.TimeResponse;
import roomescape.domain.Time;
import roomescape.service.TimeService;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    public ResponseEntity<List<TimeResponse>> times() {
        return ResponseEntity.ok(convertToTimeResponses(timeService.allTimes()));
    }

    @GetMapping(params = {"themeId", "date"})
    public ResponseEntity<List<TimeResponse>> getAvailableTimes(long themeId, LocalDate date) {
        List<Time> allTimes = timeService.allTimes();
        List<Long> reservedTimeId = timeService.findReserved(themeId, date);
        return ResponseEntity.ok(TimeResponse.availableOf(allTimes, reservedTimeId));
    }

    @PostMapping
    public ResponseEntity<TimeResponse> createTime(@RequestBody TimeRequest timeRequest) {
        Time time = timeService.saveTime(timeRequest.startAt());
        return ResponseEntity.ok(TimeResponse.from(time));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable long id) {
        timeService.removeTime(id);
        return ResponseEntity.ok().build();
    }

    private List<TimeResponse> convertToTimeResponses(List<Time> reservationTimes) {
        return reservationTimes.stream()
                .map(TimeResponse::from)
                .toList();
    }
}
