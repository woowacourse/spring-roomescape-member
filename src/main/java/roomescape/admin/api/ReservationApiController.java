package roomescape.admin.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
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
import roomescape.admin.api.dto.ReservationRequest;
import roomescape.admin.api.dto.ReservationResponse;
import roomescape.global.auth.Accessor;
import roomescape.global.auth.CustomPrincipal;
import roomescape.service.ReservationService;
import roomescape.service.result.ReservationResult;

@RestController
@RequestMapping("/admin/reservations")
@Validated
@RequiredArgsConstructor
public class ReservationApiController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> reserve(
            @CustomPrincipal Accessor accessor,
            @Valid @RequestBody ReservationRequest request
    ) {
        ReservationResult result = reservationService.reserve(accessor, request.toCommand());

        URI location = URI.create("/admin/reservations/" + result.id());

        return ResponseEntity.created(location).body(ReservationResponse.from(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(
            @CustomPrincipal
            Accessor accessor,
            @PathVariable
            @Positive(message = "예약 취소 식별자는 양수여야 합니다.")
            Long id
    ) {
        reservationService.cancelReservation(accessor, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> response = reservationService.getAllReservations()
                .stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }
}
