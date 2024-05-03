package roomescape.controller.admin;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.dto.ReservationAddRequest;
import roomescape.service.admin.AdminReservationService;

@RestController
public class AdminReservationController {

    private final AdminReservationService adminReservationService;

    public AdminReservationController(AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservationList() {
        return ResponseEntity.ok(adminReservationService.findAllReservation());
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> addReservation(@RequestBody ReservationAddRequest reservationAddRequest) {
        Reservation reservation = adminReservationService.addReservation(reservationAddRequest);
        return ResponseEntity.created(URI.create("/reservation/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> removeReservation(@PathVariable("id") Long id) {
        adminReservationService.removeReservation(id);
        return ResponseEntity.noContent().build();
    }
}
