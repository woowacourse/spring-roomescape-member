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
import roomescape.common.exception.MissingLoginException;
import roomescape.member.service.AuthService;
import roomescape.member.service.dto.LoginMemberInfo;
import roomescape.reservation.controller.dto.AdminReservationCreateRequest;
import roomescape.reservation.controller.dto.ReservationCreateRequest;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.ReservationCreateCommand;
import roomescape.reservation.service.dto.ReservationInfo;

/**
 * TODO
 * 로그인이 필요한 경우 로그인 페이지로 리다이렉트
 */
@RestController
@RequestMapping
public class ReservationController {

    private final ReservationService reservationService;
    private final AuthService authService;

    public ReservationController(final ReservationService reservationService, final AuthService authService) {
        this.reservationService = reservationService;
        this.authService = authService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationInfo> create(
            @CookieValue(value = "token", required = false) String token,
            @RequestBody @Valid final ReservationCreateRequest request
    ) {
        if (token == null) {
            throw new MissingLoginException();
        }
        LoginMemberInfo loginMemberInfo = authService.getLoginMemberInfoByToken(token);
        ReservationCreateCommand reservationCreateCommand = request.convertToCreateCommand(loginMemberInfo.id());
        final ReservationInfo reservationInfo = reservationService.createReservation(reservationCreateCommand);
        return ResponseEntity.created(URI.create("/reservations/" + reservationInfo.id())).body(reservationInfo);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationInfo> create(@RequestBody @Valid final AdminReservationCreateRequest request) {
        final ReservationInfo reservationInfo = reservationService.createReservation(request.convertToCreateCommand());
        return ResponseEntity.created(URI.create("/reservations/" + reservationInfo.id())).body(reservationInfo);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationInfo>> findAll() {
        final List<ReservationInfo> reservationInfos = reservationService.getReservations();
        return ResponseEntity.ok().body(reservationInfos);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        reservationService.cancelReservationById(id);
        return ResponseEntity.noContent().build();
    }
}
