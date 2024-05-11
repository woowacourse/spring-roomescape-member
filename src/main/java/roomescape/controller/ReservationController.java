package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.AvailableTimeResponses;
import roomescape.dto.ReservationCreateRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationResponses;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<ReservationResponses> findAllReservations() {
        ReservationResponses reservationResponses = reservationService.findAll();
        return ResponseEntity.ok()
                .body(reservationResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> findByReservationId(@PathVariable Long id) {
        ReservationResponse reservationResponse = reservationService.findByReservationId(id);
        return ResponseEntity.ok()
                .body(reservationResponse);
    }

    @GetMapping("/available")
    public ResponseEntity<AvailableTimeResponses> findAllAvailableTimes(
            @RequestParam LocalDate date, @RequestParam String themeId) {
        AvailableTimeResponses availableTimeResponses = reservationService.findByDateAndThemeId(date,
                Long.valueOf(themeId));
        return ResponseEntity.ok()
                .body(availableTimeResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody ReservationCreateRequest reservationCreateRequest) {
        ReservationResponse reservationResponse = reservationService.create(reservationCreateRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent()
                .build();
    }
}
