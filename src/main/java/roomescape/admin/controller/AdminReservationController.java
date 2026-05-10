package roomescape.admin.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.controller.dto.AdminReservationResponse;
import roomescape.admin.service.AdminReservationService;

@RequestMapping("/admin/reservations")
@RestController
@RequiredArgsConstructor
public class AdminReservationController {
    private final AdminReservationService adminReservationService;

    @GetMapping
    public List<AdminReservationResponse> getAllReservations() {
        return adminReservationService.getAllReservations().stream()
                .map(AdminReservationResponse::from)
                .toList();
    }
}
