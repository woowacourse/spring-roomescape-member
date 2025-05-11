package roomescape.reservation.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.controller.dto.LoginMember;
import roomescape.reservation.controller.dto.AdminReservationRequest;
import roomescape.reservation.controller.dto.MemberReservationRequest;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.time.controller.dto.AvailableTimeResponse;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> getReservations() {
        return reservationService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/reservations/available-times")
    public List<AvailableTimeResponse> getAvailableTimes(@RequestParam LocalDate date, @RequestParam Long themeId) {
        return reservationService.getAvailableTimes(date, themeId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/reservations")
    public ReservationResponse addMemberReservation(@RequestBody MemberReservationRequest memberRequest,
                                                    LoginMember loginMember) {
        ReservationRequest reservationRequest = new ReservationRequest(memberRequest.date(), memberRequest.timeId(),
                memberRequest.themeId(), loginMember.id());
        return reservationService.add(reservationRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/reservations")
    public ReservationResponse addAdminReservation(@RequestBody AdminReservationRequest adminRequest) {
        ReservationRequest reservationRequest = new ReservationRequest(adminRequest.date(), adminRequest.timeId(),
                adminRequest.themeId(), adminRequest.memberId());
        return reservationService.add(reservationRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/reservations/{reservationId}")
    public void deleteReservation(@PathVariable("reservationId") Long reservationId) {
        reservationService.remove(reservationId);
    }

}
