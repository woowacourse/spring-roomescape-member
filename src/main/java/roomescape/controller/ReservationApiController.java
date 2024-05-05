package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationRequest;
import roomescape.service.dto.ReservationResponse;

@RestController
@RequestMapping("/reservations")
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponse> findReservations() {
        return reservationService.findAllReservations();
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest requestDto) {
        ReservationResponse reservation = reservationService.createReservation(requestDto);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable long id) {
        reservationService.deleteReservation(id);
    }
}
