package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ReservationRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationResult;

@RestController
@RequestMapping("/user/reservations")
public class UserReservationController {

    private final ReservationService reservationService;

    public UserReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse create(@RequestBody ReservationRequest request) {
        ReservationResult saved = reservationService.create(request.toCommand());
        return ReservationResponse.from(saved);
    }
}
