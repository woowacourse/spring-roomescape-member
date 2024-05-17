package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTime>> times() {
        return ResponseEntity.ok(reservationTimeService.findAll());
    }

    @PostMapping
    public ResponseEntity<ReservationTime> create(@RequestBody ReservationTimeRequest reservationTimeRequest) {
        final ReservationTime savedReservationTime = reservationTimeService.save(reservationTimeRequest);
        return ResponseEntity.created(URI.create("/times/" + savedReservationTime.getId())).body(savedReservationTime);
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableTimeResponse>> availableTimes(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") Long themeId
    ) {
        List<AvailableTimeResponse> availableTimeResponses = reservationTimeService.findAvailableTimes(date, themeId);
        return ResponseEntity.ok(availableTimeResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
