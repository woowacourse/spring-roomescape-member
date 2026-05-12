package roomescape.presentation;

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
import roomescape.application.ReservationService;
import roomescape.domain.Reservation;
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationResponse;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> saveReservation(
            @RequestBody ReservationRequest request
    ) {
        ReservationResponse response = ReservationResponse.from(service.saveReservation(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId()
        ));
        return ResponseEntity.created(URI.create("/reservations/" + response.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long themeId
    ) {
        List<ReservationResponse> response = findReservations(date, themeId).stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    private List<Reservation> findReservations(LocalDate date, Long themeId) {
        if (date != null || themeId != null) {
            return service.getReservationsByDateAndTheme(date, themeId);
        }
        return service.getReservations();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long id
    ) {
        service.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
