package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeResponseDTO;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public List<ReservationTimeResponseDTO> readReservationTime() {
        return reservationTimeService.findAllReservationTime();
    }

    @GetMapping("/reserved")
    public List<ReservationTime> readReservedTimes(
            @RequestParam Long themeId,
            @RequestParam LocalDate selectedDate
    ) {
        return reservationTimeService.findReservedTimes(selectedDate, themeId);
    }
}
