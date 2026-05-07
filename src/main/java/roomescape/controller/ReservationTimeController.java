package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.service.ReservationTimeService;

@RequestMapping("/api/v1/times")
@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTime>> getReservationTimes() {
        List<ReservationTime> reservationTimeList = reservationTimeService.getReservationTimes();
        return ResponseEntity.ok().body(reservationTimeList);
    }

    @GetMapping(params = {"date", "themeId"})
    public ResponseEntity<List<AvailableTimeResponse>> getAvailableTimes(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam Long themeId) {
        List<AvailableTimeResponse> availableTimeResponses = reservationTimeService.getAvailableTimes(date, themeId);
        return ResponseEntity.ok().body(availableTimeResponses);
    }
}
