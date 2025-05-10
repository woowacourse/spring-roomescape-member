package roomescape.presentation.member.api;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.member.ReservationTimeService;
import roomescape.presentation.admin.dto.ReservationTimeResponseDto;
import roomescape.presentation.member.dto.AvailableTimesResponseDto;

@RestController
@RequestMapping("/times")
public final class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @Autowired
    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponseDto>> getReservationTimes() {
        List<ReservationTimeResponseDto> reservationTimes = reservationTimeService.getAllTimes();
        return ResponseEntity.ok(reservationTimes);
    }

    @GetMapping("/{timeId}")
    public ResponseEntity<ReservationTimeResponseDto> getReservationTime(@PathVariable("timeId") Long id) {
        ReservationTimeResponseDto reservationTime = reservationTimeService.getTimeById(id);
        return ResponseEntity.ok(reservationTime);
    }

    @GetMapping("/reservation")
    public ResponseEntity<List<AvailableTimesResponseDto>> getReservationAvailableTimes(
            @RequestParam("date") LocalDate date, @RequestParam("themeId") Long themeId) {
        List<AvailableTimesResponseDto> availableReservationTimes = reservationTimeService.findAvailableTimes(date,
                themeId);
        return ResponseEntity.ok(availableReservationTimes);
    }
}
