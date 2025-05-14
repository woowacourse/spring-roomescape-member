package roomescape.controller.user;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.response.ReservationTimeAvailableResponse;
import roomescape.service.ReservationTimeQueryService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeQueryService reservationTimeQueryService;

    public ReservationTimeController(ReservationTimeQueryService reservationTimeQueryService) {
        this.reservationTimeQueryService = reservationTimeQueryService;
    }

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationTimeAvailableResponse> readAvailableTimes(
            @RequestParam LocalDate date,
            @RequestParam Long themeId
    ) {
        return reservationTimeQueryService.findAvailableTimes(date, themeId);
    }
}
