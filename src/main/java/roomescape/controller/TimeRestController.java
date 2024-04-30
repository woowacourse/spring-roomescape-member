package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.TimeResponse;
import roomescape.dto.TimeSaveRequest;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/times")
public class TimeRestController {

    private final ReservationTimeService reservationTimeService;

    public TimeRestController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<TimeResponse>> getTimes() {
        List<TimeResponse> responses = reservationTimeService.findAllTimes();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<TimeResponse> createTime(@RequestBody TimeSaveRequest request) {
        TimeResponse response = reservationTimeService.saveTime(request);

        URI location = URI.create("/times/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        reservationTimeService.deleteTimeById(id);

        return ResponseEntity.noContent().build();
    }
}
