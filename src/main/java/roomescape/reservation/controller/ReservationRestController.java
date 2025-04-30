package roomescape.reservation.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.ReservationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationRestController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody final ReservationRequest reservationRequest
    ) {
        final Long id = reservationService.save(
                reservationRequest.name(),
                reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId()
        );
        final Reservation found = reservationService.getById(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservationResponse.from(found));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        final List<Reservation> reservations = reservationService.findAll();
        final List<ReservationResponse> reservationResponses = reservations.stream()
                .map(ReservationResponse::from)
                .toList();

        return ResponseEntity.ok(reservationResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable final Long id) {
        reservationService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
