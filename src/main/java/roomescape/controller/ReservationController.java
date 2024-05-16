package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.LoggedIn;
import roomescape.domain.member.AuthenticatedMember;
import roomescape.service.reservation.dto.ReservationCreateRequest;
import roomescape.service.reservation.dto.ReservationResponse;
import roomescape.service.reservation.ReservationService;

import java.util.List;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> readReservations() {
        return reservationService.readReservations();
    }

    @GetMapping("/reservations/{id}")
    public ReservationResponse readReservation(@PathVariable Long id) {
        return reservationService.readReservation(id);
    }

    @PostMapping("/reservations")
    public ReservationResponse createReservation(
            @RequestBody ReservationCreateRequest request,
            @LoggedIn AuthenticatedMember member
    ) {
        ReservationCreateRequest requestByMember = request.withMemberId(member.getId());
        return reservationService.createReservation(requestByMember);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createReservationByAdmin(
            @RequestBody ReservationCreateRequest requestByAdmin) {
        ReservationResponse response = reservationService.createReservation(requestByAdmin);
        URI location = URI.create("/reservations/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }


    @GetMapping(value = "/admin/reservations", params = {"memberId", "themeId", "startDate", "endDate"})
    public ResponseEntity<List<ReservationResponse>> findByFilter(
            @RequestParam(value = "memberId") long memberId,
            @RequestParam(value = "themeId") long themeId,
            @RequestParam(value = "startDate") LocalDate startDate,
            @RequestParam(value = "endDate") LocalDate endDate
    ) {
        List<ReservationResponse> response = reservationService.findFilteredBy(
                memberId, themeId, startDate, endDate
        );
        return ResponseEntity.ok(response);
    }
}
