package roomescape.presentation.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.UserReservationService;
import roomescape.domain.repository.dto.AvailableTimesData;

@RestController
@RequestMapping("/reservation")
public class UserReservationController {
    private final UserReservationService userReservationService;

    public UserReservationController(UserReservationService userReservationService) {
        this.userReservationService = userReservationService;
    }

    @GetMapping("/themes/{themeId}/available-times")
    public List<AvailableTimesData> themes(
            @PathVariable Long themeId,
            @RequestParam LocalDate date
    ) {
        return userReservationService.getAvailableTimes(date, themeId);
    }
}
