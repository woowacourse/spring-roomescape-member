package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationRequest;
import roomescape.service.ReservationService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
public class AdminReservationController {
    private final ReservationService reservationService;

    public AdminReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/admin/reservations")
    public ResponseEntity<List<Reservation>> reservations(
            @RequestParam("themeId") Long themeId,
            @RequestParam("memberId") Long memberId,
            @RequestParam("dateFrom") LocalDate dateFrom,
            @RequestParam("dateTo") LocalDate dateTo
    ) {
        final List<Reservation> reservations = reservationService.findReservations(themeId, memberId, dateFrom, dateTo);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<Reservation> create(@RequestBody ReservationRequest reservationRequest) {
        final Reservation reservation = reservationService.save(reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }
}
