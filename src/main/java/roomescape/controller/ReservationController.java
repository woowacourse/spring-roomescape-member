package roomescape.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/v1/reservations")
@RestController
@Validated
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> reservationResponses = reservationService.getReservations();
        return ResponseEntity.ok().body(reservationResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationCreateRequest request) {
        ReservationResponse reservationResponse = reservationService.createReservation(request);
        return ResponseEntity.created(URI.create("/api/v1/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable @NotNull(message = "예약 ID는 필수로 입력해야 합니다.") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/available-times", params = {"date", "themeId"})
    public ResponseEntity<List<AvailableTimeResponse>> getAvailableTimes(
            @RequestParam(name = "date") LocalDate date,
            @RequestParam(name = "themeId") Long id) {
        List<AvailableTimeResponse> availableTimeResponses = reservationService.getAvailableTimes(date, id);
        return ResponseEntity.ok().body(availableTimeResponses);
    }

    @GetMapping(params = {"name"})
    public ResponseEntity<List<ReservationResponse>> getUserReservations(@RequestParam String name) {
        List<ReservationResponse> reservationResponses = reservationService.getUserReservations(name);
        return ResponseEntity.ok().body(reservationResponses);
    }
}
