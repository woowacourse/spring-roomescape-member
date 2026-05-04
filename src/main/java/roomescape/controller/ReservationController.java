package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.service.ReservationService;
import roomescape.service.dto.request.ReservationCreateRequest;
import roomescape.service.dto.response.ReservationResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        final List<ReservationResponse> results = reservationService.getReservations();
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @RequestBody ReservationCreateRequest request
    ) {
        final ReservationResponse result = reservationService.create(request);
        return ResponseEntity.created(URI.create("/reservations"))
                .body(result);
    }

    @DeleteMapping("/{reservation-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("reservation-id") Long reservationId
    ) {
        reservationService.delete(reservationId);
        return ResponseEntity.noContent().build();
    }
}
