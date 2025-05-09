package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.AdminReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.AdminReservationService;

@RestController
public class AdminReservationController {

    private final AdminReservationService adminReservationService;

    public AdminReservationController(AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @PostMapping("/admin/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse createReservation(
            @RequestBody AdminReservationRequest request
    ) {
        return adminReservationService.createReservation(request);
    }
}
