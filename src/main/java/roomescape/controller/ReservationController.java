package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.service.dto.ReservationResponse;
import roomescape.service.dto.SaveReservationRequest;
import roomescape.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationFindService) {
        this.reservationService = reservationFindService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<Reservation> reservations = reservationService.findReservations();
        return ResponseEntity.ok(ReservationResponse.listOf(reservations));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody SaveReservationRequest request) {
        Reservation newReservation = reservationService.createReservation(request);
        return ResponseEntity.created(URI.create("reservations/" + newReservation.getId()))
                .body(ReservationResponse.of(newReservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.findReservations()
                .stream()
                .filter(reservation -> reservation.isSameReservation(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 아이디 입니다."));

        reservationService.deleteReservation(id);

        return ResponseEntity.noContent().build();
    }
}
