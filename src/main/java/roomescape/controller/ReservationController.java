package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ReservationRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.controller.dto.ThemeResponse;
import roomescape.controller.dto.TimeResponse;
import roomescape.domain.Reservation;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> reservations() {
        return ResponseEntity.ok(convertToReservationResponse(reservationService.allReservations()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable long id) {
        Reservation reservation = reservationService.findReservationById(id);
        return ResponseEntity.ok(toResponse(reservation));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        Reservation reservation = reservationService.saveReservation(
                request.name(), request.date(), request.timeId(), request.themeId()
        );
        return ResponseEntity.created(URI.create("/reservations/" + reservation.id()))
                .body(toResponse(reservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        reservationService.removeReservation(id);
        return ResponseEntity.noContent().build();
    }

    private List<ReservationResponse> convertToReservationResponse(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::toResponse)
                .toList();
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name(),
                reservation.date(),
                TimeResponse.from(reservation.timeSlot()),
                ThemeResponse.from(reservation.theme())
        );
    }
}
