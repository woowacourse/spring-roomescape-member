package roomescape.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.TimeRequest;
import roomescape.dto.TimeResponse;
import roomescape.service.TimeService;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    private TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping
    public TimeResponse createReservationTime(@RequestBody TimeRequest request) {
        return timeService.addReservationTime(request);
    }

    @GetMapping
    public List<TimeResponse> readReservationTimes() {
        return timeService.findAllReservationTimes();
    }

    @DeleteMapping("/{id}")
    public void deleteReservationTime(@PathVariable Long id) {
        timeService.removeReservationTime(id);
    }
}
