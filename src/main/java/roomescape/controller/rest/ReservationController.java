package roomescape.controller.rest;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.configuration.resolver.AccessToken;
import roomescape.controller.rest.request.ReservationRequest;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.service.ReservationService;

@RestController
@RequestMapping
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<Reservation> findById(@PathVariable long id) {
        Reservation reservation = reservationService.findById(id);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping("/reservations/member")
    public ResponseEntity<Reservation> createByMember(
            @AccessToken Member member,
            @RequestBody ReservationRequest request
    ) {
        Reservation createdReservation = reservationService.createByMember(member, request);
        return ResponseEntity.created(URI.create("/reservations/" + createdReservation.id())).body(createdReservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<Reservation> createByAdmin(@AccessToken Member admin,
                                                     @RequestBody ReservationRequest request) {
        Reservation createdReservation = reservationService.createByAdmin(admin, request);
        return ResponseEntity.created(URI.create("/reservations/" + createdReservation.id())).body(createdReservation);
    }
}
