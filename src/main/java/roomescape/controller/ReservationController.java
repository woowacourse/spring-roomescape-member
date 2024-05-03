package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.request.ReservationRequest;
import roomescape.controller.response.MemberReservationTimeResponse;
import roomescape.model.Reservation;
import roomescape.service.ReservationService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getReservations() {
        return ResponseEntity.ok(reservationService.findAllReservations());
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest request) {
        Reservation reservation = reservationService.addReservation(request);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/times")
    public ResponseEntity<List<MemberReservationTimeResponse>> getPossibleReservationTimes(
            @RequestParam(name = "date") LocalDate date,
            @RequestParam(name = "themeId") long themeId) {
        List<MemberReservationTimeResponse> response = reservationService.getMemberReservationTimes(date, themeId);
        return ResponseEntity.ok(response);
    }
}
