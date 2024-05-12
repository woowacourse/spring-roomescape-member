package roomescape.reservation.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.request.ReservationRequest;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody ReservationRequest request) {
        Reservation createdReservation = reservationService.save(request);
        return ResponseEntity.created(URI.create("/reservations/" + createdReservation.id())).body(createdReservation);
    }

    @PostMapping("/validate")
    public ResponseEntity<Reservation> createAndValidatePast(@RequestBody ReservationRequest request, Member member) {
        Reservation createdReservation = reservationService.validatePastAndSave(request, member);
        return ResponseEntity.created(URI.create("/reservations/" + createdReservation.id())).body(createdReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Reservation>> filter(@RequestParam long themeId, @RequestParam long memberId
            , @RequestParam String dateFrom, @RequestParam String dateTo) {
        List<Reservation> filteredReservations = reservationService.filter(themeId, memberId
                , dateFrom, dateTo);
        return ResponseEntity.ok().body(filteredReservations);
    }
}
