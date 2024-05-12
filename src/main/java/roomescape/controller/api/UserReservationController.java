package roomescape.controller.api;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.CreateReservationResponse;
import roomescape.controller.dto.CreateUserReservationRequest;
import roomescape.domain.member.LoginMember;
import roomescape.domain.reservation.Reservation;
import roomescape.global.argumentresolver.AuthenticationPrincipal;
import roomescape.service.ReservationService;
import roomescape.service.dto.SaveReservationDto;

@RestController
@RequestMapping("/reservations")
public class UserReservationController {

    private final ReservationService reservationService;

    public UserReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<CreateReservationResponse> save(
        @RequestBody CreateUserReservationRequest request,
        @AuthenticationPrincipal LoginMember member) {

        Reservation reservation = reservationService.save(new SaveReservationDto(
            member.getId(), request.date(), request.timeId(), request.themeId()));

        CreateReservationResponse response = new CreateReservationResponse(
            reservation.getId(),
            reservation.getLoginMember().getName(),
            reservation.getDate(),
            reservation.getTime().getStartAt(),
            reservation.getTheme().getName());

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId()))
            .body(response);
    }
}
