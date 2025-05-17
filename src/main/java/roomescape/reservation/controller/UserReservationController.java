package roomescape.reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomescape.member.auth.controller.dto.LoginMember;
import roomescape.reservation.controller.dto.MemberReservationRequest;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.time.controller.dto.AvailableTimeResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class UserReservationController {

    private final ReservationService reservationService;

    public UserReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReservationResponse> getReservations() {
        return reservationService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/available-times")
    public List<AvailableTimeResponse> getAvailableTimes(@RequestParam LocalDate date, @RequestParam Long themeId) {
        return reservationService.getAvailableTimes(date, themeId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReservationResponse addMemberReservation(@RequestBody MemberReservationRequest memberRequest,
                                                    LoginMember loginMember) {
        ReservationRequest reservationRequest = new ReservationRequest(memberRequest.date(), memberRequest.timeId(),
                memberRequest.themeId(), loginMember.id());
        return reservationService.add(reservationRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{reservationId}")
    public void deleteReservation(@PathVariable("reservationId") Long reservationId) {
        reservationService.remove(reservationId);
    }

}
