package roomescape.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.reservation.ReservationCancellationRequest;
import roomescape.controller.dto.reservation.ReservationRequest;
import roomescape.controller.dto.reservation.ReservationResponse;
import roomescape.controller.dto.reservation.ReservationResponses;
import roomescape.controller.dto.reservation.ReservationScheduleRequest;
import roomescape.service.ReservationService;
import roomescape.service.dto.reservation.ReservationPagingCondition;
import roomescape.service.dto.reservation.ReservationResult;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<ReservationResponses> getReservations(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        ReservationPagingCondition condition = new ReservationPagingCondition(page, size);
        List<ReservationResult> reservationResults = name == null
                ? reservationService.getReservations(condition)
                : reservationService.getReservationsByName(name);
        List<ReservationResponse> responses = reservationResults.stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.ok(new ReservationResponses(responses));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationRequest request) {
        ReservationResult reservationResult = reservationService.createReservation(request.toCommand());
        ReservationResponse response = ReservationResponse.from(reservationResult);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/schedule")
    public ResponseEntity<ReservationResponse> changeReservationSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ReservationScheduleRequest request
    ) {
        ReservationResult result = reservationService.changeReservationSchedule(request.toCommand(id));
        return ResponseEntity.ok(ReservationResponse.from(result));
    }

    @PutMapping("/{id}/cancellation")
    public ResponseEntity<ReservationResponse> cancelReservation(
            @PathVariable Long id,
            @Valid @RequestBody ReservationCancellationRequest request
    ) {
        ReservationResult result = reservationService.cancelReservation(request.toCommand(id));
        return ResponseEntity.ok(ReservationResponse.from(result));
    }
}
