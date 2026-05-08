package roomescape.reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.RequestReservation;
import roomescape.reservation.dto.ResponseReservation;
import roomescape.reservation.exception.DuplicateReservationException;
import roomescape.reservation.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseReservation>> getReservations() {
        List<Reservation> reservations = reservationService.getReservations();
        List<ResponseReservation> response = reservations.stream()
                .map(ResponseReservation::from)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler(DuplicateReservationException.class)
    @PostMapping
    public ResponseEntity<ResponseReservation> createReservation(@RequestBody RequestReservation request) {
        Reservation reservation = reservationService.createReservation(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId());
        ResponseReservation response = ResponseReservation.from(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
