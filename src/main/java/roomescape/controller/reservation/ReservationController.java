package roomescape.controller.reservation;

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
import roomescape.argumentresolver.LoginMember;
import roomescape.domain.member.MemberInfo;
import roomescape.dto.reservation.AdminReservationRequest;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservation.ReservationSearchRequest;
import roomescape.service.reservation.ReservationService;

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
        ReservationResponse response = reservationService.insertUserReservation(reservationRequest, member);
        return ResponseEntity.created(createdUri(response.id())).body(response);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createAdminReservation(
            @RequestBody @Valid AdminReservationRequest request) {
        ReservationResponse response = reservationService.insertAdminReservation(request);
        return ResponseEntity.created(createdUri(response.id())).body(response);
    }

    private static URI createdUri(Long reservationId) {
        return URI.create("/reservations/" + reservationId);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations/search")
    public ResponseEntity<List<ReservationResponse>> searchReservation(
            @ModelAttribute @Valid ReservationSearchRequest request) {
        List<ReservationResponse> response = reservationService.searchReservation(request);
        return ResponseEntity.ok().body(response);
    }
}
