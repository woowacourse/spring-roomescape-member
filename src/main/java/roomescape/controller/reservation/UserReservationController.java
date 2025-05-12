package roomescape.controller.reservation;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.member.UserReservationRequest;
import roomescape.dto.reservation.ReservationAvailableTimeResponse;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.infrastructure.intercepter.AuthenticationPrincipal;
import roomescape.infrastructure.member.MemberInfo;
import roomescape.service.reservation.ReservationService;
import roomescape.service.reservation.UserReservationTimeService;

@RestController
@RequestMapping("/user")
public class UserReservationController {

    private final UserReservationTimeService reservationTimeService;
    private final ReservationService reservationService;

    public UserReservationController(UserReservationTimeService reservationTimeService,
                                     ReservationService reservationService) {
        this.reservationTimeService = reservationTimeService;
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse addReservation(
            @AuthenticationPrincipal MemberInfo memberInfo,
            @RequestBody UserReservationRequest request,
            HttpServletResponse response) {
        ReservationResponse reservationResponse = reservationService.addReservation(request, memberInfo);
        response.setHeader("Location", "/reservations/" + reservationResponse.id());
        return reservationResponse;
    }

    @GetMapping("/times")
    public List<ReservationAvailableTimeResponse> getAvailableReservationTimes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long themeId
    ) {
        return reservationTimeService.readAvailableReservationTimes(date, themeId);
    }
}
