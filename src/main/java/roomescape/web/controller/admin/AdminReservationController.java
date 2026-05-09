package roomescape.web.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationService;
import roomescape.web.dto.ReservationRequest;
import roomescape.web.dto.ReservationResponse;
import roomescape.web.dto.ReservationResponses;

@RestController
@RequestMapping("/api/admin/reservations")
@Validated
@RequiredArgsConstructor
public class AdminReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> reserve(@Valid @RequestBody ReservationRequest request) {
        ReservationResponse response = reservationService.reserve(request);

        URI location = URI.create("/api/admin/reservations/" + response.id());

        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(
            @PathVariable
            @Positive(message = "예약 취소 식별자는 양수여야 합니다.") Long id
    ) {
        reservationService.cancel(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ReservationResponses> getAllReservations() {
        ReservationResponses response = new ReservationResponses(reservationService.getAllReservations());

        return ResponseEntity.ok(response);
    }
}
