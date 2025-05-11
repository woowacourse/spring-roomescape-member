package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.dto.response.ReservationsWithTotalPageResponse;
import roomescape.user.domain.UserPrinciple;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<ReservationsWithTotalPageResponse> getReservationsByPage(
            @RequestParam(required = false, defaultValue = "1") int page) {
        ReservationsWithTotalPageResponse reservationsWithTotalPage = reservationService.getReservationsByPage(page);
        return ResponseEntity.ok(reservationsWithTotalPage);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservation(
            @RequestBody @Valid ReservationRequest reservationRequest, UserPrinciple userPrinciple) {
        ReservationResponse newReservation = reservationService.addReservation(reservationRequest, userPrinciple);
        Long id = newReservation.id();
        return ResponseEntity.created(URI.create("/reservations/" + id)).body(newReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable(name = "id") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
