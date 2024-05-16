package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.member.dto.LoginMember;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.util.AdminRequired;

@Controller
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String getPopularPage() {
        return "index";
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> reservations() {
        return ResponseEntity.ok(reservationService.findAllReservations());
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@RequestBody @Valid ReservationRequest reservationRequest,
                                                      LoginMember member) {
        ReservationResponse response = reservationService.create(reservationRequest, member);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long reservationId) {
        reservationService.delete(reservationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservation")
    public String getReservationPage() {
        return "reservation";
    }
}
