package roomescape.presentation.reservation;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.member.LoginMemberId;
import roomescape.application.reservation.ReservationService;
import roomescape.application.reservation.dto.request.AdminReservationRequest;
import roomescape.application.reservation.dto.request.ReservationRequest;
import roomescape.application.reservation.dto.response.ReservationResponse;

@RestController
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> findAll() {
        List<ReservationResponse> responses = service.findAll();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@LoginMemberId long memberId,
                                                      @RequestBody @Valid ReservationRequest request) {
        ReservationResponse response = service.create(memberId, request);
        URI location = URI.create("/reservations/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createByAdmin(@RequestBody @Valid AdminReservationRequest request) {
        ReservationRequest reservationRequest = new ReservationRequest(
                request.date(),
                request.timeId(),
                request.themeId()
        );
        ReservationResponse response = service.create(request.memberId(), reservationRequest);
        URI location = URI.create("/reservations/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
