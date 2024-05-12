package roomescape.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.dto.AdminReservationRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> saveReservation(@RequestBody AdminReservationRequest adminReservationRequest) {
        final Reservation reservation = reservationService.saveReservation(adminReservationRequest.toReservation());

        final ReservationResponse reservationResponse = changeToReservationResponse(reservation);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponse);
    }

    private ReservationResponse changeToReservationResponse(Reservation reservation) {
        return new ReservationResponse(reservation);
    }
}
