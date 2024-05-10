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
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.request.AdminReservationWebRequest;
import roomescape.controller.response.ReservationWebResponse;
import roomescape.service.MemberAuthService;
import roomescape.service.ReservationService;
import roomescape.service.request.ReservationAppRequest;
import roomescape.service.response.MemberAppResponse;
import roomescape.service.response.ReservationAppResponse;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;
    private final MemberAuthService memberAuthService;

    public AdminReservationController(ReservationService reservationService, MemberAuthService memberAuthService) {
        this.reservationService = reservationService;
        this.memberAuthService = memberAuthService;
    }

    @PostMapping
    public ResponseEntity<ReservationWebResponse> reserve(@Valid @RequestBody AdminReservationWebRequest request) {
        MemberAppResponse memberAppresponse = memberAuthService.findMemberById(request.memberId());
        ReservationAppResponse reservationAppResponse = reservationService.save(
            new ReservationAppRequest(memberAppresponse.name(), request.date(), request.timeId(), request.themeId(),
                request.memberId()));

        Long id = reservationAppResponse.id();
        ReservationWebResponse reservationWebResponse = ReservationWebResponse.from(reservationAppResponse);

        return ResponseEntity.created(URI.create("/reservations/" + id))
            .body(reservationWebResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBy(@PathVariable Long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationWebResponse>> getReservations() {
        List<ReservationAppResponse> appResponses = reservationService.findAll();
        List<ReservationWebResponse> reservationWebResponse = appResponses.stream()
            .map(ReservationWebResponse::from)
            .toList();

        return ResponseEntity.ok(reservationWebResponse);
    }
}
