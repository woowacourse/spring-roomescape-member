package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationAvailableTimeResponse;
import roomescape.service.UserReservationTimeService;

@RestController
@RequestMapping("/user")
public class UserReservationController {

    private final UserReservationTimeService userReservationTimeService;

    public UserReservationController(UserReservationTimeService userReservationTimeService) {
        this.userReservationTimeService = userReservationTimeService;
    }

    @GetMapping("/times")
    public List<ReservationAvailableTimeResponse> readAvailableReservationTimes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long themeId
    ) {
        return userReservationTimeService.readAvailableReservationTimes(date, themeId);
    }
}
