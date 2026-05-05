package roomescape.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.facade.ReservationTimeFacade;
import roomescape.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationTimeFacade reservationTimeFacade;

    public ReservationController(ReservationService reservationService, ReservationTimeFacade reservationTimeFacade) {
        this.reservationService = reservationService;
        this.reservationTimeFacade = reservationTimeFacade;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> search() {
        List<ReservationResponse> responses = reservationService.getReservations().stream()
                .map(ReservationResponse::from)
                .toList();

        return ResponseEntity.ok().body(responses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> add(@RequestBody ReservationRequest request) {
        Reservation reservation = reservationTimeFacade.addReservation(request);
        ReservationResponse response = ReservationResponse.from(reservation);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);

        return ResponseEntity.ok().build();
    }
}
