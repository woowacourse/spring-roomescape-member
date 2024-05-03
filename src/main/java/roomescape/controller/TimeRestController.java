package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.TimeMemberResponse;
import roomescape.dto.TimeResponse;
import roomescape.dto.TimeRequest;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/times")
public class TimeRestController {

    private final ReservationTimeService reservationTimeService;

    public TimeRestController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<TimeResponse>> getAll() {
        List<TimeResponse> responses = reservationTimeService.findAll();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/member")
    public ResponseEntity<List<TimeMemberResponse>> getAll(@RequestParam LocalDate date, @RequestParam Long themeId) {
        List<TimeMemberResponse> responses = reservationTimeService.findAllWithBooking(date, themeId);

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<TimeResponse> create(@RequestBody TimeRequest request) {
        TimeResponse response = reservationTimeService.save(request);

        URI location = URI.create("/times/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.deleteTimeById(id);

        return ResponseEntity.noContent().build();
    }
}
