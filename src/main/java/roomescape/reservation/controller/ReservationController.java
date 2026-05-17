package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.payload.ReservationRequest;
import roomescape.reservation.payload.ReservationResponse;
import roomescape.reservation.payload.ReservationUpdateRequest;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse postReservation(@Valid @RequestBody ReservationRequest request) {
        Reservation reservation = reservationService.save(request);
        return ReservationResponse.from(reservation);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> getReservations(@RequestParam String name) {
        return reservationService.findByName(name)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable Long id, @RequestParam String name) {
        reservationService.cancelByIdAndName(id, name);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponse updateReservation(@PathVariable Long id,
                                                 @RequestParam String name,
                                                 @Valid @RequestBody ReservationUpdateRequest request) {
        Reservation reservation = reservationService.updateByIdAndName(id, name, request);
        return ReservationResponse.from(reservation);
    }
}
