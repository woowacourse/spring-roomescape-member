package roomescape.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.Reservation.ReservationCommand;
import roomescape.dto.Reservation.ReservationCondition;
import roomescape.dto.Reservation.AddReservationRequest;
import roomescape.dto.Reservation.ReservationResponse;
import roomescape.service.RoomReservationService;

@RestController
@Profile("web")
@RequestMapping("/reservations")
public class ReservationController {
    private final RoomReservationService roomReservationService;

    public ReservationController(RoomReservationService roomReservationService) {
        this.roomReservationService = roomReservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<Reservation> reservations = roomReservationService.getAllReservation();
        List<ReservationResponse> reservationResponses = reservations.stream()
                .map(ReservationResponse::from)
                .toList();

        return new ResponseEntity<>(reservationResponses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody @Valid AddReservationRequest addReservationRequest) {
        ReservationCommand reservationCommand = addReservationRequest.to();
        Reservation addedReservation = roomReservationService.addReservation(reservationCommand);

        return new ResponseEntity<>(ReservationResponse.from(addedReservation), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") long id) {
        roomReservationService.deleteReservation(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(params = {"name"})
    public ResponseEntity<List<ReservationResponse>> getReservation(@ModelAttribute @Valid ReservationCondition reservationCondition) {
        List<Reservation> reservations = roomReservationService.getAllReservationByName(reservationCondition.name());
        List<ReservationResponse> reservationResponses = reservations.stream()
                .map(ReservationResponse::from)
                .toList();

        return new ResponseEntity<>(reservationResponses, HttpStatus.OK);
    }
}
