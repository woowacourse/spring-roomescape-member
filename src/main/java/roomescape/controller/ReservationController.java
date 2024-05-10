package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.dto.reservation.AdminReservationCreateRequest;
import roomescape.dto.reservation.AvailableReservationResponse;
import roomescape.dto.reservation.MemberReservationCreateRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping("/available-time")
    public ResponseEntity<List<AvailableReservationResponse>> readReservationTimes(@RequestParam String date,
                                                                                   @RequestParam Long themeId) {
        return ResponseEntity.ok(reservationService.findTimeByDateAndThemeID(date, themeId));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createByMember(@RequestBody @Valid MemberReservationCreateRequest request,
                                                              Member member) {
        ReservationResponse newReservation = reservationService.add(request, member);
        return ResponseEntity.created(URI.create("/reservations/" + newReservation.id()))
                .body(newReservation);
    }

    @PostMapping("/admin")
    public ResponseEntity<ReservationResponse> create(@RequestBody @Valid AdminReservationCreateRequest request) {
        ReservationResponse newReservation = reservationService.add(request);
        return ResponseEntity.created(URI.create("/reservations/" + newReservation.id()))
                .body(newReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
