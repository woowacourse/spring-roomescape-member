package roomescape.controller.admin;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController(value = "AdminReservationController")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> getReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long id
    ) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
