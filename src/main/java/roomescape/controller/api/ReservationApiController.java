package roomescape.controller.api;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.api.dto.request.MemberAuthRequest;
import roomescape.controller.api.dto.request.ReservationCreateForUserRequest;
import roomescape.controller.api.dto.response.ReservationResponse;
import roomescape.controller.api.resolver.AuthMember;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody final ReservationCreateForUserRequest request,
                                                                 @AuthMember final MemberAuthRequest member) {
        final var output = reservationService.createReservation(request.toInput(member.id()));
        return ResponseEntity.created(URI.create("/reservations/" + output.id()))
                .body(ReservationResponse.from(output));
    }
}
