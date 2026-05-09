package roomescape.domain.reservation.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.dto.ReservationRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationResponses;
import roomescape.facade.ReservationFacade;
import roomescape.domain.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationFacade reservationFacade;

    public ReservationController(ReservationService reservationService, ReservationFacade reservationFacade) {
        this.reservationService = reservationService;
        this.reservationFacade = reservationFacade;
    }

    @GetMapping
    public ResponseEntity<ReservationResponses> search() {
        return ResponseEntity.ok().body(ReservationResponses.from(reservationService.getReservations()));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> add(@RequestBody ReservationRequest request) {
        Reservation reservation = reservationFacade.addReservation(request);
        ReservationResponse response = ReservationResponse.from(reservation);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);

        return ResponseEntity.ok().build();
    }
}
