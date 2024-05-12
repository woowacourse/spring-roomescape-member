package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.resolver.LoginMember;
import roomescape.member.domain.MemberInfo;
import roomescape.reservation.dto.reservation.AdminReservationRequest;
import roomescape.reservation.dto.reservation.ReservationRequest;
import roomescape.reservation.dto.reservation.ReservationResponse;
import roomescape.reservation.dto.reservation.ReservationSearchRequest;
import roomescape.reservation.service.ReservationService;

@Controller
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createMemberReservation(
            @RequestBody @Valid ReservationRequest reservationRequest,
            @LoginMember MemberInfo member) {
        ReservationResponse response = reservationService.insertReservation(reservationRequest, member);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createAdminReservation(
            @RequestBody @Valid AdminReservationRequest request) {
        ReservationResponse response = reservationService.insertAdminReservation(request);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations/search")
    public ResponseEntity<List<ReservationResponse>> searchReservation(@ModelAttribute @Valid ReservationSearchRequest request) {
        List<ReservationResponse> response = reservationService.searchReservation(request);
        return ResponseEntity.ok().body(response);
    }
}
