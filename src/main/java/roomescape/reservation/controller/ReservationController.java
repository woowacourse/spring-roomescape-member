package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RoleRequired;
import roomescape.auth.dto.LoginMember;
import roomescape.member.entity.Role;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationAvailableTimeResponse;
import roomescape.reservation.dto.ReservationCommand;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationsByFilterRequest;
import roomescape.reservation.service.ReservationService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> postReservation(@RequestBody ReservationRequest request, LoginMember loginMember) {
        ReservationCommand command = request.toCommand(loginMember.id());
        ReservationResponse reservationResponse = reservationService.createReservation(command);
        URI location = URI.create("/reservations/" + reservationResponse.id());
        return ResponseEntity.created(location).body(reservationResponse);
    }

    @PostMapping("/admin/reservations")
    @RoleRequired(Role.ADMIN)
    public ResponseEntity<ReservationResponse> postReservation(@RequestBody AdminReservationRequest request) {
        ReservationResponse reservationResponse = reservationService.createReservation(request.toCommand());
        URI location = URI.create("/admin/reservations/" + reservationResponse.id());
        return ResponseEntity.created(location).body(reservationResponse);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        return ResponseEntity.ok(reservationService.readReservations());
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservationById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations/times")
    public List<ReservationAvailableTimeResponse> getAvailableReservationTimes(
            @RequestParam LocalDate date, @RequestParam long themeId
    ) {
        return reservationService.readAvailableReservationTimes(date, themeId);
    }

    @GetMapping("/reservations/filter")
    @RoleRequired(Role.ADMIN)
    public ResponseEntity<List<ReservationResponse>> getReservationsByFilter(
            @ModelAttribute ReservationsByFilterRequest request
    ) {
        List<ReservationResponse> reservationResponses = reservationService.readAllByFilter(request);
        return ResponseEntity.ok(reservationResponses);
    }
}
