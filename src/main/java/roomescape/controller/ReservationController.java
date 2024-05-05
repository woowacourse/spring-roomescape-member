package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.reservation.AvailableReservationResponse;
import roomescape.dto.reservation.ReservationCreateRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping("/user")
    public ResponseEntity<List<AvailableReservationResponse>> readReservationTimes(@RequestParam String date,
                                                                                   @RequestParam Long themeId,
                                                                                   @RequestParam LocalDate today,
                                                                                   @RequestParam LocalTime now) {
        return ResponseEntity.ok(reservationService.findTimeByDateAndThemeID(date, themeId, today, now));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationCreateRequest request) {
        return ResponseEntity.created(URI.create("/reservations"))
                .body(reservationService.add(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
