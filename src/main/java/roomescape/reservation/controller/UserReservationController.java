package roomescape.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.AuthenticatedMember;
import roomescape.auth.annotation.AuthenticationPrincipal;
import roomescape.reservation.controller.dto.request.CreateReservationUserRequest;
import roomescape.reservation.controller.dto.response.ReservationResponse;
import roomescape.reservation.application.UserReservationService;
import roomescape.reservation.application.dto.response.ReservationServiceResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class UserReservationController {

    private final UserReservationService userReservationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReservationResponse create(
            @RequestBody @Valid CreateReservationUserRequest request,
            @AuthenticationPrincipal AuthenticatedMember authenticatedMember
    ) {
        Long memberId = authenticatedMember.id();
        String memberName = authenticatedMember.name();
        ReservationServiceResponse response = userReservationService.create(request.toServiceRequest(memberName, memberId));
        return ReservationResponse.from(response);
    }
}
