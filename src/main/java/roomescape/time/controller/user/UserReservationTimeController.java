package roomescape.time.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.service.dto.response.AvailableReservationTimeResponse;
import roomescape.time.service.ReservationTimeService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class UserReservationTimeController {
    private final ReservationTimeService timeService;

    public UserReservationTimeController(ReservationTimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/available-times")
    public List<AvailableReservationTimeResponse> getAvailableTimes(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId")Long themeId
    ) {
        return timeService.getAvailableTimes(date, themeId);
    }
}
