package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.Reservation;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> readAll() {
        List<ReservationResponse> reservations = reservationService.findAll().stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(reservations);
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponse> readById(@PathVariable Long id) {
        Reservation reservation = reservationService.findById(id);
        return ResponseEntity.ok().body(ReservationResponse.from(reservation));
    }

    @PostMapping("/reservations")
    public ResponseEntity<?> create(@Valid @RequestBody ReservationRequest request) {
        try {
            Reservation reservation = reservationService.add(
                    request.name(),
                    request.themeId(),
                    request.date(),
                    request.timeId()
            );

            URI location = URI.create("/reservation/" + reservation.getId());

            return ResponseEntity.created(location).body(ReservationResponse.from(reservation));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 불가");
        }
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
