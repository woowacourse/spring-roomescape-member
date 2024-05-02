package roomescape.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.controller.ReservationController;
import roomescape.reservation.service.ReservationService;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
public class UserController {

    private final ReservationService reservationService;

    public UserController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


}
