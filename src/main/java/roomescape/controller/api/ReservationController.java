package roomescape.controller.api;

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
import roomescape.controller.LoginMemberArgument;
import roomescape.domain.Member;
import roomescape.dto.AdminReservationRequest;
import roomescape.dto.MemberReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> responses = reservationService.getAllReservations();

        return ResponseEntity.ok()
                .body(responses);
    }

    @GetMapping("/reservations/filter")
    public ResponseEntity<List<ReservationResponse>> getReservationsWithFilter(
            @RequestParam Long memberId, @RequestParam Long themeId, @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo) {
        List<ReservationResponse> responses = reservationService.getReservationsWithFilter(memberId, themeId, dateFrom,
                dateTo);

        return ResponseEntity.ok()
                .body(responses);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody MemberReservationRequest request,
                                                              @LoginMemberArgument Member member) {
        ReservationResponse response = reservationService.addReservation(request, member);
        URI location = URI.create("/reservations/" + response.id());

        return ResponseEntity.created(location)
                .body(response);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> addAdminReservation(@RequestBody AdminReservationRequest request) {
        ReservationResponse response = reservationService.addAdminReservation(request);
        URI location = URI.create("/admin/reservations/" + response.id());

        return ResponseEntity.created(location)
                .body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);

        return ResponseEntity.noContent()
                .build();
    }
}
