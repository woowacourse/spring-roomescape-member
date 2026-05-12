package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.reservation.ReservationInfo;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.dto.Response;
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

    @GetMapping
    public ResponseEntity<Response> getReservations(@RequestParam(required = false) String name) {
        List<ReservationInfo> reservations = roomReservationService.getAllReservation(name);
        List<ReservationResponse> reservationResponses = reservations.stream()
                .map(ReservationResponse::from)
                .toList();

        return ResponseEntity.ok(Response.from(HttpStatus.OK.value(), reservationResponses));
    }

    @PostMapping
    public ResponseEntity<Response> addReservation(@RequestBody @Valid AddReservationRequest addReservationRequest) {
        ReservationCommand reservationCommand = addReservationRequest.to();
        ReservationInfo addedReservation = roomReservationService.addReservation(reservationCommand);

        return new ResponseEntity<>(Response.from(HttpStatus.CREATED.value(), ReservationResponse.from(addedReservation)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@RequestHeader(required = false) String name, @PathVariable("id") long id) {
        roomReservationService.deleteReservation(id, URLDecoder.decode(name, StandardCharsets.UTF_8));

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
