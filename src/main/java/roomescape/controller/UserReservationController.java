package roomescape.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ReservationRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.service.UserReservationService;
import roomescape.service.dto.ReservationResult;

@RestController
@RequestMapping("/user/reservations")
public class UserReservationController {

    private final UserReservationService userReservationService;

    public UserReservationController(UserReservationService userReservationService) {
        this.userReservationService = userReservationService;
    }

    @PostMapping
    public ReservationResponse create(@RequestBody ReservationRequest request) {
        ReservationResult saved = userReservationService.create(request.toCommand());
        return ReservationResponse.from(saved);
    }
}
