package roomescape.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.LoginMember;
import roomescape.dto.request.UserReservationRequest;
import roomescape.global.resolver.CurrentMember;
import roomescape.model.Reservation;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/user/reservations")
public class UserReservationController {
    private final ReservationService reservationService;

    public UserReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Reservation> addReservation(@RequestBody UserReservationRequest reservationRequestDto,
                                                      @CurrentMember LoginMember loginMember) {
        Reservation reservation = reservationService.addReservationByUser(reservationRequestDto, loginMember);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservation);
    }

}
