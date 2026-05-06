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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.TimeRequest;
import roomescape.controller.dto.TimeResponse;
import roomescape.domain.Time;
import roomescape.service.TimeService;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService reservationTimeService;

    public TimeController(TimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

//    @GetMapping
//    public ResponseEntity<List<TimeResponse>> times() {
//        return ResponseEntity.ok(convertToTimeResponses(reservationTimeService.allTimes()));
//    }

    @GetMapping
    public ResponseEntity<List<TimeResponse>> getAvailableTimes(@RequestParam("themeId") long themeId,
                                                                @RequestParam("date") LocalDate date) {
        List<Time> allTimes = reservationTimeService.allTimes();
        List<Long> reservedTimeId = reservationTimeService.findReserved(themeId, date);
        return ResponseEntity.ok(TimeResponse.availableOf(allTimes, reservedTimeId));
    }

    @PostMapping
    public ResponseEntity<TimeResponse> createTime(
            @RequestBody TimeRequest timeRequest) {
        Time time = reservationTimeService.saveTime(timeRequest.startAt());
        return ResponseEntity.ok(TimeResponse.from(time));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable long id) {
        reservationTimeService.removeTime(id);
        return ResponseEntity.ok().build();
    }

    private List<TimeResponse> convertToTimeResponses(List<Time> reservationTimes) {
        return reservationTimes.stream()
                .map(TimeResponse::from)
                .toList();
    }
}
