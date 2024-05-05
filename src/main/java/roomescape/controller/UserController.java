package roomescape.controller;

import org.springframework.web.bind.annotation.RestController;

import roomescape.service.ReservationService;

@RestController
public class UserController {

    private final ReservationService reservationService;

    public UserController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


}
