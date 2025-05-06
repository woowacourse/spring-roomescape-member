package roomescape.domain.reservation.controller;

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

import jakarta.validation.Valid;
import roomescape.domain.reservation.dto.BookedReservationTimeResponse;
import roomescape.domain.reservation.dto.ReservationRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.service.ReservationService;

@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readAllReservations() {
        List<ReservationResponse> response = reservationService.getAll();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    public ResponseEntity<List<BookedReservationTimeResponse>> readAvailableReservationTimes(
            @RequestParam("date") final LocalDate date,
            @RequestParam("themeId") final Long themeId
    ) {
        List<BookedReservationTimeResponse> responses = reservationService.getAvailableTimes(date, themeId);

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody final ReservationRequest request) {
        ReservationResponse response = reservationService.create(request);

        return ResponseEntity.created(URI.create("/reservations/" + response.id()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
