package roomescape.reservation.controller;

import jakarta.validation.Valid;
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
import roomescape.cofig.LoginMember;
import roomescape.member.Member;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> findAll(
            @RequestParam(required = false) Long themeId,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo
    ) {
        List<ReservationResponse> all = reservationService.findAll(themeId, memberId, dateFrom, dateTo);
        return ResponseEntity.ok(all);
    }

    @PostMapping("/reservations")
    public ResponseEntity<Long> create(
            @LoginMember Member member,
            @Valid @RequestBody ReservationCreateRequest reservationCreateRequest) {
        final ReservationResponse response = reservationService.createReservation(reservationCreateRequest, member);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response.id());
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createAdmin(
            @Valid @RequestBody ReservationRequest reservationRequest
    ) {
        final ReservationResponse response = reservationService.createReservation(reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Integer> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
