package roomescape.controller.api;

import org.springframework.web.bind.annotation.*;
import roomescape.common.dto.ApiResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/times")
@RestController
public class UserReservationTimeRestController {

    private final ReservationTimeService reservationTimeService;

    public UserReservationTimeRestController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ApiResponse<List<ReservationTimeResponse>> read(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long themeId
    ) {
        return new ApiResponse<>(reservationTimeService.read(date, themeId));
    }
}
