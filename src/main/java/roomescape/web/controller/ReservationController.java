package roomescape.web.controller;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationQueryService;
import roomescape.service.ReservationService;
import roomescape.service.auth.AuthenticatedMemberProfile;
import roomescape.service.request.ReservationQueryRequest;
import roomescape.service.response.ReservationResponse;
import roomescape.web.controller.request.ReservationWebRequest;
import roomescape.web.security.Authenticated;

@RestController
@RequestMapping("/reservations")
@Validated
class ReservationController {

    private final ReservationService reservationService;
    private final ReservationQueryService reservationQueryService;

    public ReservationController(
            ReservationService reservationService,
            ReservationQueryService reservationQueryService
    ) {
        this.reservationService = reservationService;
        this.reservationQueryService = reservationQueryService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Authenticated AuthenticatedMemberProfile profile,
            @Valid @RequestBody ReservationWebRequest request
    ) {
        ReservationResponse response = reservationService.createReservation(request.toServiceRequest(profile.id()));
        URI uri = URI.create("/reservations/" + response.id());

        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readReservations(
            @Valid @ModelAttribute ReservationQueryRequest request
    ) {
        List<ReservationResponse> responses = reservationQueryService.getReservations(request);

        return ResponseEntity.ok(responses);
    }
}
