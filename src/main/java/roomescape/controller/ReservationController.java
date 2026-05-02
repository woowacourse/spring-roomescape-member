package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.request.ReservationCreateRequest;
import roomescape.controller.dto.response.ReservationResponse;
import roomescape.service.ReservationService;
import roomescape.service.dto.response.ReservationResult;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        final List<ReservationResult> results = reservationService.getReservations();
        return ResponseEntity.ok(ReservationResponse.from(results));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @RequestBody ReservationCreateRequest request
    ) {
        final ReservationResult result = reservationService.create(request.toData());
        return ResponseEntity.ok(ReservationResponse.from(result));
    }

    @DeleteMapping("/{reservation-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("reservation-id") Long reservationId
    ) {
        reservationService.delete(reservationId);
        return ResponseEntity.noContent().build();
    }
}
