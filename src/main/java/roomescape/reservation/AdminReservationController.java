package roomescape.reservation;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private final AdminReservationService adminReservationService;

    public AdminReservationController(AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createForceReservation(@Valid @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = adminReservationService.createForceReservation(reservationRequest.themeId(),
                reservationRequest.name(), reservationRequest.date(), reservationRequest.timeId());

        return ResponseEntity.status(HttpStatus.CREATED).body(ReservationResponse.from(reservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteForceReservation(@PathVariable long id) {
        adminReservationService.forceDeleteReservation(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
