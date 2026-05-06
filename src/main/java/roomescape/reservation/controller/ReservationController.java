package roomescape.reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.dto.RequestReservation;
import roomescape.reservation.dto.ResponseReservation;
import roomescape.reservation.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ResponseReservation> getReservations() {
        return reservationService.getReservations()
                .stream()
                .map(ResponseReservation::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ResponseReservation> createReservation(@RequestBody RequestReservation request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseReservation.from(reservationService.createReservation(
                        request.name(),
                        request.date(),
                        request.timeId(),
                        request.themeId()
            )));
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }
}
