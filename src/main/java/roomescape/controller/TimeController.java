package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationAvailableTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final ReservationTimeService reservationTimeService;

    public TimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(
            @Validated @RequestBody ReservationTimeRequest request) {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationTimeResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getAll() {
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.findAll();
        return ResponseEntity.ok().body(reservationTimeResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<ReservationAvailableTimeResponse>> getTimesOfTheme(
            @RequestParam("themeId") Long themeId,
            @RequestParam("date") LocalDate date
    ) {
        return ResponseEntity.ok().body(reservationTimeService.findAvailableTimes(themeId, date));
    }
}
