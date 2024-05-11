package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.helper.LoginMember;
import roomescape.domain.Member;
import roomescape.service.ReservationService;
import roomescape.service.dto.AdminReservationRequest;
import roomescape.service.dto.ReservationRequest;
import roomescape.service.dto.ReservationResponse;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> findAllReservation(
            @RequestParam(required = false, name = "member-id") Long memberId,
            @RequestParam(required = false, name = "theme-id") Long themeId,
            @RequestParam(required = false, name = "date-from") LocalDate dateFrom,
            @RequestParam(required = false, name = "date-to") LocalDate dateTo) {
        List<ReservationResponse> response = reservationService.findAllReservation(memberId, themeId, dateFrom, dateTo);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> saveReservation(@RequestBody ReservationRequest request,
                                                               @LoginMember Member member) {
        ReservationResponse response = reservationService.saveReservation(request, member);
        return ResponseEntity.created(URI.create("/reservations/" + response.getId())).body(response);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> saveReservationByAdmin(@RequestBody AdminReservationRequest request) {
        ReservationResponse response = reservationService.saveReservationByAdmin(request);
        return ResponseEntity.created(URI.create("/reservations/" + response.getId())).body(response);
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
