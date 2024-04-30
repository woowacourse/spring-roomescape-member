package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.service.dto.ReservationResponse;
import roomescape.service.dto.SaveReservationRequest;
import roomescape.service.reservation.ReservationCreateService;
import roomescape.service.reservation.ReservationDeleteService;
import roomescape.service.reservation.ReservationFindService;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationFindService reservationFindService;
    private final ReservationCreateService reservationCreateService;
    private final ReservationDeleteService reservationDeleteService;

    public ReservationController(ReservationFindService reservationFindService,
                                 ReservationCreateService reservationCreateService,
                                 ReservationDeleteService reservationDeleteService) {
        this.reservationFindService = reservationFindService;
        this.reservationCreateService = reservationCreateService;
        this.reservationDeleteService = reservationDeleteService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<Reservation> reservations = reservationFindService.findReservations();
        return ResponseEntity.ok(ReservationResponse.listOf(reservations));
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody SaveReservationRequest request) {
        Reservation newReservation = reservationCreateService.createReservation(request);
        return ResponseEntity.created(URI.create("reservations/" + newReservation.getId()))
                .body(ReservationResponse.of(newReservation));
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationFindService.findReservations()
                .stream()
                .filter(reservation -> reservation.isSameReservation(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 아이디 입니다."));

        reservationDeleteService.deleteReservation(id);

        return ResponseEntity.noContent().build();
    }
}
