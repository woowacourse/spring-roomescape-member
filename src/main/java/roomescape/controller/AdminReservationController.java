package roomescape.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.reservation.ReservationResponse;
import roomescape.controller.dto.reservation.ReservationResponses;
import roomescape.service.ReservationService;
import roomescape.service.dto.reservation.ReservationPagingCondition;

@RestController
@RequestMapping("/admin/reservations")
@RequiredArgsConstructor
public class AdminReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<ReservationResponses> getReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        ReservationPagingCondition condition = new ReservationPagingCondition(page, size);
        List<ReservationResponse> responses = reservationService.getReservations(condition).stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.ok(new ReservationResponses(responses));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}
