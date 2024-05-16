package roomescape.controller.reservation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.security.Permission;
import roomescape.domain.member.Role;
import roomescape.domain.reservation.Reservation;
import roomescape.service.dto.reservation.AdminReservationSaveRequest;
import roomescape.service.dto.reservation.ReservationResponse;
import roomescape.service.reservation.ReservationService;

import java.net.URI;

@RestController
@RequestMapping("/admin/reservations")
@Permission(role = Role.ADMIN)
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationFindService) {
        this.reservationService = reservationFindService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody @Valid AdminReservationSaveRequest request) {
        Reservation newReservation = reservationService.createReservation(request);
        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId()))
                .body(ReservationResponse.of(newReservation));
    }
}
