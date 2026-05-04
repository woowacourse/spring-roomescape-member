package roomescape.time.controller;

import org.springframework.web.bind.annotation.*;
import roomescape.time.dto.RequestReservationTime;
import roomescape.time.dto.ResponseReservationTime;
import roomescape.time.service.ReservationTimeService;

import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public List<ResponseReservationTime> getTimes() {
        return reservationTimeService.getTimes()
                .stream()
                .map(ResponseReservationTime::from)
                .toList();
    }

    @PostMapping
    public ResponseReservationTime createTime(@RequestBody RequestReservationTime request) {
        return ResponseReservationTime.from(reservationTimeService.createTime(request.startAt()));
    }

    @DeleteMapping("/{id}")
    public void removeTime(@PathVariable Long id) {
        reservationTimeService.removeTime(id);
    }
}
