package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Reservation;
import roomescape.dto.AdminReservationRequestDto;
import roomescape.dto.FilterConditionDto;
import roomescape.service.ReservationService;

import java.net.URI;
import java.util.List;

@Controller
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok().body(reservations);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<Reservation> createAdminReservation(@RequestBody AdminReservationRequestDto adminReservationRequestDto) {
        Reservation reservation = reservationService.insertReservation(adminReservationRequestDto);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @GetMapping("/reservations/filter")
    public ResponseEntity<List<Reservation>> getFilteredReservations(@RequestBody FilterConditionDto filterConditionDto) {
        List<Reservation> reservations = reservationService.getFilteredReservations(filterConditionDto);
        return ResponseEntity.ok().body(reservations);
    }
}
