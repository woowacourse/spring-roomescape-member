package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.service.AuthService;
import roomescape.member.service.dto.MemberInfo;
import roomescape.reservation.controller.dto.CreateReservationInfo;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final AuthService authService;

    public ReservationController(final ReservationService reservationService, final AuthService authService) {
        this.reservationService = reservationService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @CookieValue("token") String token,
            @RequestBody @Valid final ReservationRequest request
    ) {
        MemberInfo memberInfo = authService.getMemberInfoByToken(token);
        CreateReservationInfo createReservationInfo = request.convertToCreateReservationInfo(memberInfo.id());
        final ReservationResponse response = reservationService.createReservation(createReservationInfo);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAll() {
        final List<ReservationResponse> responses = reservationService.getReservations();
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        reservationService.cancelReservationById(id);
        return ResponseEntity.noContent().build();
    }
}
