package roomescape.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ReservationTimeRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ReservationTimeResponse createReservationTime(
            @RequestBody final ReservationTimeRequest reservationTimeRequest) {
        return reservationTimeService.createReservationTime(reservationTimeRequest);
    }

    @GetMapping
    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeService.getReservationTimes();
    }

    @DeleteMapping("/{id}")
    public void deleteReservationTime(@PathVariable("id") final long id) {
        reservationTimeService.deleteReservationTime(id);
    }
}
