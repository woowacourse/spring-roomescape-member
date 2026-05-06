package roomescape.reservation.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.dto.request.ReservationStatusUpdateDto;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/admin")
public class ReservationAdminController {
    private final ReservationService reservationService;

    public ReservationAdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> read() {
        return ResponseEntity.ok(reservationService.readAll());
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationSaveDto dto) {
        return ResponseEntity.ok(reservationService.create(dto));
    }

    @PatchMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.cancel(id));
    }
}
