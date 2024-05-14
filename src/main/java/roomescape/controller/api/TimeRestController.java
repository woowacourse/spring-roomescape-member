package roomescape.controller.api;

import jakarta.validation.Valid;
import java.net.URI;
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
import roomescape.dto.TimeCreateRequest;
import roomescape.dto.TimeMemberResponse;
import roomescape.dto.TimeResponse;
import roomescape.service.ReservationTimeService;

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
    public ResponseEntity<Long> create(@Valid @RequestBody TimeCreateRequest request) {
        long id = reservationTimeService.save(request);

        URI location = URI.create("/times/" + id);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.deleteTimeById(id);

        return ResponseEntity.noContent().build();
    }
}
