package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ReservationService;
import roomescape.service.ThemeService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final ThemeService themeService;

    public ReservationController(ReservationService reservationService, ThemeService themeService) {
        this.reservationService = reservationService;
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationSaveRequest request) {
        ThemeResponse themeResponse = themeService.findById(request.themeId());
        Reservation reservation = request.toModel(themeResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(reservation));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findReservations() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
