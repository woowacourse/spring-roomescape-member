package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.reservation.ReservationWithTimeAndTheme;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.dto.reservation.AddReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservation.UpdateReservationRequest;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations(@RequestParam(required = false) String name) {
        List<ReservationWithTimeAndTheme> reservations = reservationService.getAllReservation(name);
        List<ReservationResponse> reservationResponses = reservations.stream()
                .map(ReservationResponse::from)
                .toList();

        return ResponseEntity.ok(reservationResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody @Valid AddReservationRequest addReservationRequest) {
        ReservationCommand reservationCommand = addReservationRequest.to();
        ReservationWithTimeAndTheme addedReservation = reservationService.addReservation(reservationCommand);

        return new ResponseEntity<>(ReservationResponse.from(addedReservation), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@RequestHeader(required = false) String name, @PathVariable("id") long id) {
        reservationService.deleteReservation(id, URLDecoder.decode(name, StandardCharsets.UTF_8));

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> updateReservation(@RequestHeader(required = false) String name, @PathVariable("id") long id,
                                                      @RequestBody @Valid UpdateReservationRequest updateReservationRequest) {
        ReservationCommand reservationCommand = updateReservationRequest.to();
        reservationService.updateReservation(id, URLDecoder.decode(name, StandardCharsets.UTF_8), reservationCommand);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
