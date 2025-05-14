package roomescape.controller.user;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.other.TimeWithBookState;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.TimeWithBookStateResponse;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public List<ReservationTimeResponse> getReservationTimes() {
        List<ReservationTime> times = reservationTimeService.getAllReservationTime();
        return times.stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    @GetMapping(params = {"date", "themeId"})
    public List<TimeWithBookStateResponse> getReservationTimesInThemeAndDate(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") Long themeId
    ) {
        List<TimeWithBookState> times = reservationTimeService
                .getAllReservationTimeWithBookState(date, themeId);
        return times.stream()
                .map(TimeWithBookStateResponse::new)
                .toList();
    }
}
