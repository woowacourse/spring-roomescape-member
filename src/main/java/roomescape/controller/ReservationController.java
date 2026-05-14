package roomescape.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.reservation.CancelReservationRequest;
import roomescape.dto.reservation.CreateReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservation.ReservationResponses;
import roomescape.service.ReservationService;

@Validated
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<ReservationResponses> readReservations(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(required = false) @Size(min = 1) String name
    ) {
        return ResponseEntity.ok(reservationService.getReservations(page, size, name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> readReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(ReservationResponse.from(reservationService.getReservation(id)));
    }

    @PostMapping
    public ResponseEntity<Void> createReservation(
            @Valid @RequestBody CreateReservationRequest createReservationRequest) {
        Reservation createdReservation = reservationService.createReservation(createReservationRequest);

        URI location = URI.create("/reservations/" + createdReservation.getId());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Long id,
            @Valid @RequestBody CancelReservationRequest request) {
        reservationService.cancelOwnReservation(id, request.name());
        return ResponseEntity.ok().build();
    }
}