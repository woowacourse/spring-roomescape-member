package roomescape.web.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationService;
import roomescape.web.dto.reservation.ReservationRequest;
import roomescape.web.dto.reservation.ReservationResponse;
import roomescape.web.dto.reservation.ReservationResponses;

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
            @Positive(message = "예약 식별자는 양수여야 합니다.") Long id
    ) {
        reservationService.cancel(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ReservationResponses> getAllReservations(
            @RequestParam
            @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다.") int page,
            @RequestParam
            @Positive(message = "조회 개수는 양수여야 합니다.") int size
    ) {
        ReservationResponses response = new ReservationResponses(
                reservationService.getAllReservationsByPaging(page, size));

        return ResponseEntity.ok(response);
    }
}
