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
import roomescape.member.service.dto.LoginMemberInfo;
import roomescape.reservation.service.dto.ReservationCreateCommand;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.service.dto.ReservationInfo;
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
    public ResponseEntity<ReservationInfo> create(
            @CookieValue("token") String token,
            @RequestBody @Valid final ReservationRequest request
    ) {
        LoginMemberInfo loginMemberInfo = authService.getLoginMemberInfoByToken(token);
        ReservationCreateCommand reservationCreateCommand = request.convertToCreateCommand(loginMemberInfo.id());
        final ReservationInfo reservationInfo = reservationService.createReservation(reservationCreateCommand);
        return ResponseEntity.created(URI.create("/reservations/" + reservationInfo.id())).body(reservationInfo);
    }

    @GetMapping
    public ResponseEntity<List<ReservationInfo>> findAll() {
        final List<ReservationInfo> reservationInfos = reservationService.getReservations();
        return ResponseEntity.ok().body(reservationInfos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        reservationService.cancelReservationById(id);
        return ResponseEntity.noContent().build();
    }
}
