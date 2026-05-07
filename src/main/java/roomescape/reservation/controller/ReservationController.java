package roomescape.reservation.controller;

import java.net.URI;
import java.time.LocalDate;
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
import roomescape.reservation.controller.dto.ReservationCreateRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.reservationtime.controller.dto.ReservationTimeResponse;
import roomescape.theme.controller.dto.ThemeResponse;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> reservations = reservationService.getAll().stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationCreateRequest reservationRequest) {
        ReservationResponse reservation = ReservationResponse.from(reservationService.save(
                reservationRequest.name(),
                reservationRequest.date(),
                reservationRequest.timeId()
        ));

        return ResponseEntity.created(URI.create("/reservations/" + reservation.id()))
                .body(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/theme/{themeId}/times/available-times")
    public ResponseEntity<List<ReservationTimeResponse>> getAvailableTimes(
            @PathVariable final Long themeId,
            @RequestParam final LocalDate date
    ) {
        return ResponseEntity.ok()
                .body(reservationService.findAvailableTimes(date, themeId).stream()
                        .map(ReservationTimeResponse::from)
                        .toList());
    }

    @GetMapping("/theme/popular")
    public ResponseEntity<List<ThemeResponse>> getPopularThemes(
            @RequestParam final int period,
            @RequestParam final int limit
    ) {
        return ResponseEntity.ok()
                .body(reservationService.getPopularThemes(period, limit).stream()
                        .map(ThemeResponse::from)
                        .toList());
    }

}
