package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.repository.dto.ReservationTimesWithStatus;
import roomescape.service.ReservationService;
import roomescape.service.dto.request.ReservationCreateRequest;
import roomescape.service.dto.response.ReservationOptionResponse;
import roomescape.service.dto.response.ReservationResponse;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping(path = "/available-times", params = {"date", "themeId"})
    public ResponseEntity<List<ReservationTimesWithStatus>> getReservationTimeStatuses(
            @RequestParam(value = "date", required = false) LocalDate date,
            @RequestParam(value = "themeId", required = false) Long themeId
    ) {
        final List<ReservationTimesWithStatus> results = reservationService.getReservationTimeStatuses(date, themeId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/date-and-theme")
    public ResponseEntity<ReservationOptionResponse> getReservationOptions() {
        final ReservationOptionResponse results = reservationService.getReservationOptions();
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @RequestBody ReservationCreateRequest request
    ) {
        final ReservationResponse result = reservationService.create(request);
        return ResponseEntity.created(URI.create("/reservations"))
                .body(result);
    }

    @DeleteMapping("/{reservation-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("reservation-id") Long reservationId
    ) {
        reservationService.delete(reservationId);
        return ResponseEntity.noContent().build();
    }
}
