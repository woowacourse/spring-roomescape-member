package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.AdminReservationResponse;
import roomescape.service.AdminReservationService;

@RequestMapping("/admin/reservations")
@RestController
public class AdminReservationController {
    private final AdminReservationService adminReservationService;

    public AdminReservationController(AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @GetMapping
    public ResponseEntity<List<AdminReservationResponse>> getAllReservations() {
        final List<AdminReservationResponse> reservations = adminReservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }
}
