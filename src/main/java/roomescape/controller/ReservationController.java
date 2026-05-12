package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.*;
import roomescape.domain.Reservation;
import roomescape.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> reservations() {
        return ResponseEntity.ok(convertToReservationResponse(reservationService.allReservations()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable long id) {
        Reservation reservation = reservationService.findReservationById(id);
        return ResponseEntity.ok(toResponse(reservation));
    }

    @GetMapping(params = {"userName"})
    public ResponseEntity<List<ReservationResponse>> getReservationByName(@RequestParam String userName) {
        return ResponseEntity.ok(convertToReservationResponse(reservationService.findReservationByName(userName)));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Valid ReservationRequest request) {
        Reservation reservation = reservationService.saveReservation(
                request.name(), request.date(), request.timeId(), request.themeId()
        );
        return ResponseEntity.created(URI.create("/reservations/" + reservation.id()))
                .body(toResponse(reservation));
    }

    @DeleteMapping(value = "/{id}", params = {"userName"})
    public ResponseEntity<Void> deleteReservation(@PathVariable long id, @RequestParam String userName) {
        reservationService.removeReservation(id, userName);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}", params = {"userName"})
    public ResponseEntity<ReservationResponse> updateReservation(
            @PathVariable long id,
            @RequestBody @Valid ReservationPutRequest request,
            @RequestParam String userName
    ) {
        reservationService.putReservation(id, userName, request.name(), request.date(), request.timeId(), request.themeId());
        return ResponseEntity.ok(toResponse(reservationService.findReservationById(id)));
    }

    @PatchMapping(value = "/{id}", params = {"userName"})
    public ResponseEntity<ReservationResponse> patchReservation(
            @PathVariable long id,
            @RequestBody ReservationPatchRequest request,
            @RequestParam String userName
    ) {
        reservationService.patchReservation(id, userName, request.name(), request.date(), request.timeId(), request.themeId());
        return ResponseEntity.ok(toResponse(reservationService.findReservationById(id)));
    }

    private List<ReservationResponse> convertToReservationResponse(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::toResponse)
                .toList();
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name(),
                reservation.date(),
                TimeResponse.from(reservation.timeSlot()),
                ThemeResponse.from(reservation.theme())
        );
    }
}
