package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.dto.reservation.AddReservationRequest;
import roomescape.dto.reservation.ReservationCondition;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.service.RoomReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final RoomReservationService roomReservationService;

    public ReservationController(RoomReservationService roomReservationService) {
        this.roomReservationService = roomReservationService;
    }

    @GetMapping()
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<Reservation> reservations = roomReservationService.getAllReservation();
        List<ReservationResponse> reservationResponses = reservations.stream()
                .map(ReservationResponse::from)
                .toList();

        return ResponseEntity.ok(reservationResponses);
    }

    @PostMapping()
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody @Valid AddReservationRequest addReservationRequest) {
        Reservation addedReservation = roomReservationService.addReservation(addReservationRequest);

        return new ResponseEntity<>(ReservationResponse.from(addedReservation), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") long id) {
        roomReservationService.deleteReservation(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(params = {"name"})
    public ResponseEntity<List<ReservationResponse>> getReservation(@ModelAttribute @Valid ReservationCondition reservationCondition) {
        List<Reservation> reservations = roomReservationService.getAllReservationByName(reservationCondition.name());
        List<ReservationResponse> reservationResponses = reservations.stream()
                .map(ReservationResponse::from)
                .toList();

        return ResponseEntity.ok(reservationResponses);
    }
}
