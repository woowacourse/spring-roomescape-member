package roomescape.domain.reservation.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.login.controller.AuthenticationPrincipal;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.dto.ReservationAddRequest;
import roomescape.domain.reservation.service.AdminReservationService;

@RestController
public class AdminReservationController {

    private final AdminReservationService adminReservationService;

    public AdminReservationController(AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<Reservation> addReservation(@RequestBody ReservationAddRequest reservationAddRequest,
                                                      @AuthenticationPrincipal Member member) {
        reservationAddRequest = new ReservationAddRequest(reservationAddRequest.date(), reservationAddRequest.timeId(),
                reservationAddRequest.themeId(), member.getId());
        Reservation reservation = adminReservationService.addReservation(reservationAddRequest);
        return ResponseEntity.created(URI.create("/reservation/" + reservation.getId())).body(reservation);
    }
}
