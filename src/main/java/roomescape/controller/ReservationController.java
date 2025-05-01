package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.PastReservationException;
import roomescape.exception.ReservationTimeConflictException;
import roomescape.service.ReservationService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readAllReservations() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping("/themes/{themeId}/times")
    public ResponseEntity<List<AvailableReservationTimeResponse>> readAvailableReservationTimes(
        @PathVariable Long themeId,
        @RequestParam LocalDate date) {
        return ResponseEntity.ok(reservationService.findAvailableReservationTime(themeId, date));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> add(@Valid @RequestBody ReservationRequest requestDto) {
        return new ResponseEntity<>(reservationService.add(requestDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({
        PastReservationException.class,
        DuplicateReservationException.class,
        ReservationTimeConflictException.class})
    public ResponseEntity<String> handleCreateReservationException(final RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
