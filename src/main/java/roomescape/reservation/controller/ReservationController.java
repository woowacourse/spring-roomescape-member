package roomescape.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationUpdateRequest;
import roomescape.reservation.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationCreateRequest request) {
        Reservation reservation = reservationService.createReservation(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId());
        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable("id") Long id) {
        Reservation reservation = reservationService.getReservation(id);
        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getUserReservations(@RequestParam("name") String name) {
        List<Reservation> userReservation = reservationService.getUserReservations(name);
        List<ReservationResponse> response = userReservation.stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelUserReservation(
            @PathVariable("id") Long id,
            @RequestParam("name") String name
    ) {
        reservationService.cancelUserReservation(id, name);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateUserReservation(
            @PathVariable("id") Long id,
            @RequestParam("name") String name,
            @Valid @RequestBody ReservationUpdateRequest request
            ) {
        Reservation reservation = reservationService.updateUserReservation(
                id, name, request.date(), request.timeId()
        );
        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
