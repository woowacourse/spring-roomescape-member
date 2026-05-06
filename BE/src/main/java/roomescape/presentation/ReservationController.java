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
import roomescape.entity.Reservation;
import roomescape.presentation.dto.AvailableReservationResponse;
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
        Reservation created = service.saveReservation(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId()
        );

        ReservationResponse response = ReservationResponse.from(created);
        return ResponseEntity.created(URI.create("/reservations/" + created.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<Reservation> reservations = service.getReservations();

        List<ReservationResponse> response = parseToReservationResponse(reservations);

        return ResponseEntity.ok(response);
    }

    @GetMapping(params = {"date", "themeId"})
    public ResponseEntity<List<AvailableReservationResponse>> getReservationsByDateAndThemeId(
            @RequestParam LocalDate date,
            @RequestParam Long themeId
    ) {
        List<Reservation> reservations = service.getReservationsByDateAndTheme(date, themeId);
        List<AvailableReservationResponse> response = parseToAvailableReservationResponse(reservations);

        return ResponseEntity.ok(response);
    }

    private List<ReservationResponse> parseToReservationResponse(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private List<AvailableReservationResponse> parseToAvailableReservationResponse(List<Reservation> reservations) {
        return reservations.stream()
                .map(AvailableReservationResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long id
    ) {
        service.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
