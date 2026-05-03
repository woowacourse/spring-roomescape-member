package roomescape.reservation;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> getReservations() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ReservationResponse createReservation(@RequestBody ReservationRequest request) {
        return reservationService.save(request);
    }

    @DeleteMapping("/reservations/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
    }
}
