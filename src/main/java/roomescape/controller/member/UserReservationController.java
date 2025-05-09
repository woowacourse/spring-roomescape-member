package roomescape.controller.member;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.reservation.ReservationAvailableTimeResponse;
import roomescape.service.memeber.UserReservationTimeService;

@RestController
@RequestMapping("/user")
public class UserReservationController {

    private final UserReservationTimeService reservationTimeService;

    public UserReservationController(UserReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public List<ReservationAvailableTimeResponse> readAvailableReservationTimes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long themeId
    ) {
        return reservationTimeService.readAvailableReservationTimes(date, themeId);
    }
}
