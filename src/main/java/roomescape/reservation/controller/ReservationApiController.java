package roomescape.reservation.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.RequestReservation;
import roomescape.reservation.dto.ResponseReservation;
import roomescape.reservation.service.ReservationService;

@RestController
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ResponseReservation>> findAll() {
        List<ResponseReservation> responseReservations = reservationService.findAll();

        return ResponseEntity.ok(responseReservations);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ResponseReservation> create(@RequestBody RequestReservation requestReservation) {
        Long id = reservationService.save(requestReservation);
        ResponseReservation responseReservation = reservationService.findOneById(id);
        return ResponseEntity.ok(responseReservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        reservationService.delete(id);

        return ResponseEntity.ok().build();
    }
}
