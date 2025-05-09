package roomescape.reservation.presentation;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.login.presentation.controller.dto.annotation.LoginAdmin;
import roomescape.auth.login.presentation.controller.dto.LoginAdminInfo;
import roomescape.reservation.presentation.dto.AdminReservationRequest;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.reservation.presentation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@RestController
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody AdminReservationRequest request,
            @LoginAdmin LoginAdminInfo adminInfo)
    {
        ReservationResponse response = reservationService.createReservation(
                new ReservationRequest(request.date(), request.timeId(), request.themeId()), request.memberId());
        return ResponseEntity.created(URI.create("/admin/reservation")).body(response);
    }
}
