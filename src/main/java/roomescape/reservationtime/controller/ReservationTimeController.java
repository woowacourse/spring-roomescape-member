package roomescape.reservationtime.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.dto.AvailableTimeResponse;
import roomescape.reservationtime.dto.ReservationTimeRequest;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeDefaultService;

@RestController
@RequestMapping("times")
public class ReservationTimeController {
    private final ReservationTimeDefaultService timeDefaultService;

    public ReservationTimeController(ReservationTimeDefaultService timeDefaultService) {
        this.timeDefaultService = timeDefaultService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(
            @Valid @RequestBody ReservationTimeRequest request) {
        ReservationTimeResponse response = timeDefaultService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        return ResponseEntity.ok(timeDefaultService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        timeDefaultService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableTimeResponse>> getAvailableReservationTimes(
            @RequestParam LocalDate date,
            @RequestParam Long themeId) {
        return ResponseEntity.ok(timeDefaultService.getAvailableTimes(date, themeId));
    }
}
