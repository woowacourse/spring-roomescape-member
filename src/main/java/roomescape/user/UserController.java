package roomescape.user;

import org.springframework.web.bind.annotation.RestController;

import roomescape.reservation.service.ReservationService;

@RestController
public class UserController {

    private final ReservationService reservationService;

    public UserController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


}
