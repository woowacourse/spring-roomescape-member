package roomescape.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.config.annotation.AuthMember;
import roomescape.domain.Member;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.custom.AuthenticatedException;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private static final String ADMIN_NAME = "admin";
    private static final String ADMIN_EMAIL = "admin";
    private static final String ADMIN_PASSWORD = "1234";

    private final ReservationService reservationService;

    private AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse createReservation(
        @AuthMember Member member,
        @RequestBody ReservationRequest request) {
        validateAdmin(member);

        return ReservationResponse.from(reservationService.addReservation(request));
    }

    private void validateAdmin(Member member) {
        if (!member.getName().equals(ADMIN_NAME)
            || !member.getEmail().equals(ADMIN_EMAIL)
            || !member.getPassword().equals(ADMIN_PASSWORD)) {
            throw new AuthenticatedException("not admin");
        }
    }
}
