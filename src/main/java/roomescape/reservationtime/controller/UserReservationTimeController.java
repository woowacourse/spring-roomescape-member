package roomescape.reservationtime.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservationtime.domain.AvailableTime;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.dto.ScheduleResponse;
import roomescape.reservationtime.service.UserReservationTimeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/times")
public class UserReservationTimeController {
    private final UserReservationTimeService userReservationTimeService;

    public UserReservationTimeController(UserReservationTimeService userReservationTimeService) {
        this.userReservationTimeService = userReservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        List<ReservationTime> reservationTimes = userReservationTimeService.getReservationTimes();
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
