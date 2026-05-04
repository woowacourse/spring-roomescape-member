package roomescape.reservation;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponse> getReservations() {
        List<Reservation> reservations = reservationService.getReservations();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();

    }

    @PostMapping
    public ReservationResponse createReservation(@Valid @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.createReservation(reservationRequest.name(),
                reservationRequest.date(), reservationRequest.timeId(), reservationRequest.themeId());
        return ReservationResponse.from(reservation);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable long id, @RequestBody String name) {
        reservationService.deleteReservation(id, name);
    }
}
