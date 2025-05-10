package roomescape.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.ReservationPostRequestByUser;
import roomescape.dto.response.ReservationPostResponse;
import roomescape.service.ReservationCommandService;
import roomescape.web.LoginMember;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationCommandService reservationCommandService;

    public ReservationController(ReservationCommandService reservationCommandService) {
        this.reservationCommandService = reservationCommandService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationPostResponse createReservation(
            @RequestBody ReservationPostRequestByUser request,
            LoginMember member
    ) {
        return reservationCommandService.createReservationOfLoginMember(request, member);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(
            @PathVariable long id
    ) {
        reservationCommandService.deleteReservation(id);
    }
}
