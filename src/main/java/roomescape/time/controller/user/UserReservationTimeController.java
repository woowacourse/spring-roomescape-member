package roomescape.time.controller.user;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.service.ReservationTimeService;
import roomescape.time.service.dto.response.ReservationTimeWithBookedResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservation")
public class UserReservationTimeController {
    private final ReservationTimeService timeService;

    public UserReservationTimeController(ReservationTimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/times")
    public List<ReservationTimeWithBookedResponse> getAvailableTimes(
            @RequestParam("date") @NotNull LocalDate date,
            @RequestParam("themeId") @NotNull Long themeId
    ) {
        return timeService.getAllTimesWithBooked(date, themeId);
    }
}
