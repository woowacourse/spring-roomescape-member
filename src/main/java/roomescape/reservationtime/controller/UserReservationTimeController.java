package roomescape.reservationtime.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.domain.AvailableTime;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.UserReservationTimeService;

@RestController
@RequestMapping("/times")
public class UserReservationTimeController {
    private final UserReservationTimeService userReservationTimeService;

    public UserReservationTimeController(UserReservationTimeService userReservationTimeService) {
        this.userReservationTimeService = userReservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        List<ReservationTime> reservationTimes = userReservationTimeService.findReservationTimes();
        List<ReservationTimeResponse> response = reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{themeId}")
    public ResponseEntity<ScheduleResponse> getSchedules(@PathVariable long themeId,
                                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        List<AvailableTime> availableTimes = userReservationTimeService.getSchedules(date, themeId);
        return ResponseEntity.ok().body(ScheduleResponse.from(themeId, date, availableTimes));
    }


}
