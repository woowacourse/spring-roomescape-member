package roomescape.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.service.ReservationTimeService;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ReservationTimeSaveRequest;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(@RequestBody ReservationTimeSaveRequest request) {
        var reservationTime = request.toModel();
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationTimeService.create(reservationTime));
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findReservationTimes() {
        return ResponseEntity.ok(reservationTimeService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableReservationTimeResponse>> findAllByDateAndThemeId(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam Long themeId) {
        return ResponseEntity.ok(reservationTimeService.findAvailableReservationTimes(date, themeId));
    }
}
