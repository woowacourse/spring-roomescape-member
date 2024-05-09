package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.annotation.AuthenticationPrincipal;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.dto.AdminReservationRequestDto;
import roomescape.dto.ReservationRequestDto;
import roomescape.service.ReservationService;

import java.net.URI;
import java.util.List;

@Controller
public class ReservationResponseController {

    private final ReservationService reservationService;

    public ReservationResponseController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@AuthenticationPrincipal Member member, @RequestBody ReservationRequestDto reservationRequestDto) {
        Reservation reservation = reservationService.insertReservation(member.getName(), reservationRequestDto);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<Reservation> createAdminReservation(@RequestBody AdminReservationRequestDto adminReservationRequestDto) {
        Reservation reservation = reservationService.insertAdminReservation(adminReservationRequestDto);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }
}
