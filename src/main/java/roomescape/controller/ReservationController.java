package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.annotation.AuthenticationPrinciple;
import roomescape.domain.Reservation;
import roomescape.dto.other.AuthenticationInformation;
import roomescape.dto.request.ReservationCreationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> getReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
            @AuthenticationPrinciple AuthenticationInformation authenticationInformation,
            @Valid @RequestBody ReservationCreationRequest request
    ) {
        long id = reservationService.saveReservation(authenticationInformation.id(), request);
        Reservation savedReservation = reservationService.getReservationById(id);
        return ResponseEntity
                .created(URI.create("/reservations/" + id))
                .body(new ReservationResponse(savedReservation));
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long id
    ) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
