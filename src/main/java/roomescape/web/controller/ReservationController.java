package roomescape.web.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.core.dto.reservation.ReservationRequest;
import roomescape.core.dto.reservation.ReservationResponse;
import roomescape.core.dto.reservation.ReservationsResponse;
import roomescape.core.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody final ReservationRequest request) {
        validateRequest(request);
        final ReservationResponse result = reservationService.create(request);
        return ResponseEntity.created(URI.create("/reservations/" + result.getId()))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<ReservationsResponse> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void validateRequest(final ReservationRequest request) {
        final String date = request.getDate();
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("Date cannot be null or empty");
        }

        final String name = request.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        if (request.getTimeId() == null) {
            throw new IllegalArgumentException("Time ID cannot be null");
        }

        if (request.getThemeId() == null) {
            throw new IllegalArgumentException("Theme ID cannot be null");
        }
    }
}
