package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.AdminReservationResponse;
import roomescape.service.ReservationService;

@RequestMapping("/admin/reservations")
@RestController
public class AdminReservationController {
    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<AdminReservationResponse>> getAllReservations() {
        final List<AdminReservationResponse> reservations = reservationService.findAll()
                .stream()
                .map(r -> AdminReservationResponse.from(r, r.getTheme()))
                .toList();
        return ResponseEntity.ok(reservations);
    }
}
