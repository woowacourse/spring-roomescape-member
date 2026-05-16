package roomescape.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.AdminReservationService;
import roomescape.reservationtime.dto.dto.ReservationRequest;
import roomescape.reservationtime.dto.dto.ReservationResponse;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private final AdminReservationService adminReservationService;

    public AdminReservationController(AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createForceReservation(
            @Valid @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = adminReservationService.forceCreateReservation(reservationRequest.themeId(),
                reservationRequest.name(), reservationRequest.date(), reservationRequest.timeId());

        return ResponseEntity.status(HttpStatus.CREATED).body(ReservationResponse.from(reservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForceReservation(@PathVariable long id) {
        adminReservationService.forceDeleteReservation(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
