package roomescape.admin.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.controller.dto.AdminReservationResponse;
import roomescape.admin.service.AdminReservationService;

@RequestMapping("/admin/reservations")
@RestController
public class AdminReservationController {
    private final AdminReservationService adminReservationService;

    public AdminReservationController(AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @GetMapping
    public ResponseEntity<List<AdminReservationResponse>> getAllReservations() {
        final List<AdminReservationResponse> list = adminReservationService.getAllReservations();
        return ResponseEntity.ok(list);
    }
}
