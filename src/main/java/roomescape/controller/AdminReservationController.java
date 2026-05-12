package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationResponseDTO;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/admin")
public class AdminReservationController {

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    private final ReservationService reservationService;

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponseDTO>> readAll() {
        return ResponseEntity.ok(reservationService.readAllReservation());
    }

}
