package roomescape.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.dto.reservation.ReservationCondition;
import roomescape.dto.reservation.AddReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.service.RoomReservationService;

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
        ReservationCommand reservationCommand = addReservationRequest.to();
        Reservation addedReservation = roomReservationService.addReservation(reservationCommand);

        return ResponseEntity.ok(ReservationResponse.from(addedReservation));
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
