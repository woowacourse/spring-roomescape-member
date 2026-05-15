package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.dto.UpdateReservationRequest;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/api")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<ReservationsResponse> findReservationsByName(@RequestParam String name) {
        List<ReservationResponse> reservations = reservationService.findByName(name).stream()
                .map(ReservationResponse::from)
                .toList();

        return ResponseEntity.status(200).body(new ReservationsResponse(reservations, reservations.size()));
    }

    @PostMapping("/reservations")
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationRequest request) {
        try {
            Reservation reservation = reservationService.addReservation(
                    request.name(),
                    request.date(),
                    request.timeId(),
                    request.themeId()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(ReservationResponse.from(reservation));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 불가");
        }
    }

    @PatchMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponse> updateReservation(@PathVariable Long id, @RequestParam String name, @RequestBody UpdateReservationRequest request) {
        reservationService.update(id, name, request.date(), request.timeId(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id, @RequestParam String name) {
        reservationService.delete(id, name, LocalDateTime.now());
        return ResponseEntity.noContent().build();
    }
}
