package roomescape.controller.api;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.ReservationService;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.auth.LoginInfo;
import roomescape.dto.reservation.AdminReservationRequest;
import roomescape.dto.reservation.AdminReservationResponse;
import roomescape.dto.reservation.ReservationInfo;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<AdminReservationResponse> createReservation(
            @RequestBody AdminReservationRequest reservationRequest,
            LoginInfo loginInfo) {

        ReservationInfo reservationInfo = new ReservationInfo(
                loginInfo.name(),
                reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId()
        );
        Reservation reservation = reservationService.addReservation(reservationInfo);

        AdminReservationResponse response = AdminReservationResponse.from(reservation);
        URI location = URI.create("/admin/reservations/" + response.id());

        return ResponseEntity.created(location).body(response);
    }
}
