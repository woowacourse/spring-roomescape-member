package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.reservation.AdminReservationResponse;
import roomescape.controller.dto.reservation.AdminReservationsResponse;
import roomescape.service.ReservationService;

@RequestMapping("/admin/reservations")
@RestController
public class AdminReservationController {
    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<AdminReservationsResponse> getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<AdminReservationResponse> reservations = reservationService.findAll(page, size)
                .stream()
                .map(r -> AdminReservationResponse.from(r, r.getTheme()))
                .toList();
        long totalCount = reservationService.countAll();
        return ResponseEntity.ok(new AdminReservationsResponse(reservations, totalCount, page, size));
    }
}
