package roomescape.controller.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.service.ReservationService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponse> getReservations() {
        return reservationService.getReservations();
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody final ReservationRequest request) {
        final ReservationResponse reservation = reservationService.addReservation(request);
        final URI uri = UriComponentsBuilder.fromPath("/reservations/{id}")
                .buildAndExpand(reservation.id())
                .toUri();

        return ResponseEntity.created(uri)
                .body(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationsData(@PathVariable("id") final Long id) {
        final int deletedCount = reservationService.deleteReservation(id);
        if (deletedCount == 0) {
            return ResponseEntity.notFound()
                    .build();
        }
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/popular-themes")
    public List<PopularThemeResponse> getPopularThemes() {
        return reservationService.getPopularThemes(LocalDate.now());
    }
}
