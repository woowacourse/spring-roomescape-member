package roomescape.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import roomescape.controller.api.dto.request.CreateReservationRequest;
import roomescape.controller.api.dto.response.ReservationResponse;
import roomescape.service.UserReservationService;
import roomescape.service.dto.response.ReservationServiceResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class UserReservationController {

    private final UserReservationService userReservationService;

    // TODO : Admin과 API를 공유하고 있다. 분리를 고민해보자.
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReservationResponse create(@RequestBody CreateReservationRequest request) {
        ReservationServiceResponse response = userReservationService.create(request.toServiceRequest());
        return ReservationResponse.from(response);
    }
}
