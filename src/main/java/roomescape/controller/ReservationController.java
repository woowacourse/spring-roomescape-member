package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ReservationTimeWebResponse;
import roomescape.controller.dto.ReservationWebRequest;
import roomescape.controller.dto.ReservationWebResponse;
import roomescape.controller.dto.ThemeWebResponse;
import roomescape.domain.Reservation;
import roomescape.exception.PastReservationException;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationAppRequest;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationWebResponse> reserve(@RequestBody ReservationWebRequest request) {
        Reservation newReservation = reservationService.save(
            new ReservationAppRequest(request.name(), request.date(), request.timeId(), request.themeId()));
        Long id = newReservation.getId();

        ReservationWebResponse reservationWebResponse = new ReservationWebResponse(id, newReservation.getName(),
            newReservation.getReservationDate(),
            ReservationTimeWebResponse.from(newReservation),
            ThemeWebResponse.from(newReservation));

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
            map(reservation -> new ReservationWebResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getReservationDate(),
                ReservationTimeWebResponse.from(reservation),
                ThemeWebResponse.from(reservation)
            )).toList();

        return ResponseEntity.ok(reservationWebResponse);
    }

    @ExceptionHandler(PastReservationException.class)
    public ResponseEntity<String> handlePastReservationException(PastReservationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
