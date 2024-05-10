package roomescape.core.controller;

import jakarta.validation.Valid;
import java.net.URI;
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
import roomescape.core.dto.reservationtime.BookedTimeResponse;
import roomescape.core.dto.reservationtime.ReservationTimeRequest;
import roomescape.core.dto.reservationtime.ReservationTimeResponse;
import roomescape.core.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(@Valid @RequestBody final ReservationTimeRequest request) {
        final ReservationTimeResponse response = reservationTimeService.create(request);
        return ResponseEntity.created(URI.create("/times/" + response.getId()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findAll() {
        return ResponseEntity.ok(reservationTimeService.findAll());
    }

    @GetMapping(params = {"date", "theme"})
    public ResponseEntity<List<BookedTimeResponse>> findAllWithBookable(@RequestParam("date") String date,
                                                                        @RequestParam("theme") Long themeId) {
        return ResponseEntity.ok(reservationTimeService.findAllWithBookable(date, themeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
