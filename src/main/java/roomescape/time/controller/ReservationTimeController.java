package roomescape.time.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomescape.time.controller.dto.ReservationTimeRequest;
import roomescape.time.controller.dto.ReservationTimeResponse;
import roomescape.time.service.ReservationTimeService;

import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReservationTimeResponse> getTimes() {
        return reservationTimeService.getTimes();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReservationTimeResponse addTime(@RequestBody ReservationTimeRequest request) {
        return reservationTimeService.add(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{timeId}")
    public void deleteTime(@PathVariable("timeId") Long timeId) {
        reservationTimeService.remove(timeId);
    }

}
