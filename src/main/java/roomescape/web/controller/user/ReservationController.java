package roomescape.web.controller.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationService;
import roomescape.web.dto.reservation.ReservationCancelRequest;
import roomescape.web.dto.reservation.ReservationRequest;
import roomescape.web.dto.reservation.ReservationResponse;
import roomescape.web.dto.reservation.ReservationResponses;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> reserve(@Valid @RequestBody ReservationRequest request) {
        ReservationResponse response = reservationService.reserve(request);

        URI location = URI.create("/api/reservations/" + response.id());

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<ReservationResponses> getReservationsByUser(@RequestParam String name) {
        ReservationResponses response = new ReservationResponses(reservationService.getReservationsByUser(name));

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> cancel(
            @PathVariable
            @Positive(message = "예약 식별자는 양수여야 합니다.") Long id,
            @RequestBody ReservationCancelRequest request
    ) {
        reservationService.cancel(id, request);

        return ResponseEntity.noContent().build();
    }
}
