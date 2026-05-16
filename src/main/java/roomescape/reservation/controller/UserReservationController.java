package roomescape.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationDeleteRequest;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationUpdateRequest;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class UserReservationController {
    private final ReservationService reservationService;

    public UserReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations(
            @RequestParam(required = true) String name
    ) {
        List<Reservation> reservations = reservationService.findAllByName(name);

        List<ReservationResponse> response = reservations.stream()
                .map(ReservationResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.save(
                reservationRequest.name(),
                reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId()
        );

        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable long id,
            @Valid @RequestBody ReservationDeleteRequest reservationDeleteRequest) {
        reservationService.deleteByUser(id, reservationDeleteRequest.name());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateReservation(
            @PathVariable long id,
            @Valid @RequestBody ReservationUpdateRequest updateRequest){

        Reservation updateReservation = reservationService.updateReservationDateTimeByUser(
                id,
                updateRequest.name(),
                updateRequest.date(),
                updateRequest.timeId()
        );

        return ResponseEntity.ok(ReservationResponse.from(updateReservation));
    }
}
