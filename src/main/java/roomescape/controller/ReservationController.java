package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.app.ReservationAppRequest;
import roomescape.dto.web.ReservationTimeWebResponse;
import roomescape.dto.web.ReservationWebRequest;
import roomescape.dto.web.ReservationWebResponse;
import roomescape.exception.reservation.DuplicatedReservationException;
import roomescape.exception.reservation.IllegalDateFormatException;
import roomescape.exception.reservation.IllegalReservationFormatException;
import roomescape.exception.reservation.PastReservationException;
import roomescape.exception.reservation.ReservationTimeNotFoundException;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationWebResponse> reserve(@RequestBody ReservationWebRequest request) {
        Reservation newReservation = reservationService.save(
            new ReservationAppRequest(request.timeId(), request.date(), request.name()));
        Long id = newReservation.getId();

        ReservationWebResponse reservationWebResponse = new ReservationWebResponse(id, newReservation.getName(),
            newReservation.getDate(),
            ReservationTimeWebResponse.from(newReservation));

        return ResponseEntity.created(URI.create("/reservations/" + id))
            .body(reservationWebResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBy(@PathVariable Long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationWebResponse>> getReservations() {
        List<Reservation> reservations = reservationService.findAll();
        List<ReservationWebResponse> reservationWebResponse = reservations.stream().
            map(reservation -> new ReservationWebResponse(reservation.getId(), reservation.getName(),
                reservation.getDate(), ReservationTimeWebResponse.from(reservation)))
            .toList();

        return ResponseEntity.ok(reservationWebResponse);
    }

    @ExceptionHandler(IllegalDateFormatException.class)
    public ResponseEntity<String> handleIllegalDateFormatException(IllegalDateFormatException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ReservationTimeNotFoundException.class)
    public ResponseEntity<String> handleReservationTimeNotFoundException(ReservationTimeNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(PastReservationException.class)
    public ResponseEntity<String> handlePastReservationException(PastReservationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedReservationException.class)
    public ResponseEntity<String> handleDuplicatedReservationException(DuplicatedReservationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalReservationFormatException.class)
    public ResponseEntity<String> handleIllegalReservationFormatException(IllegalReservationFormatException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
