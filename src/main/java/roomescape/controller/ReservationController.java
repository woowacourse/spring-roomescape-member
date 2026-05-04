package roomescape.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> read() {
        return reservationService.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @PostMapping("/reservations")
    public ReservationResponse create(@RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.save(
                reservationRequest.name(),
                reservationRequest.date(),
                reservationRequest.timeId()
        );
        return ReservationResponse.from(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public void delete(@PathVariable Long id) {
        reservationService.delete(id);
    }
}
