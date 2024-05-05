package roomescape.controller.api;

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
import roomescape.controller.api.dto.request.ReservationRequest;
import roomescape.controller.api.dto.response.ReservationResponse;
import roomescape.service.ReservationService;
import roomescape.service.dto.output.ReservationOutput;

@RestController
@RequestMapping("/reservations")
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody final ReservationRequest request) {
        final ReservationOutput output = reservationService.createReservation(request.toInput());
        return ResponseEntity.created(URI.create("/reservations/" + output.id()))
                             .body(ReservationResponse.toResponse(output));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        final List<ReservationOutput> outputs = reservationService.getAllReservations();
        return ResponseEntity.ok(ReservationResponse.toResponses(outputs));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable final long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent()
                             .build();
    }
}
