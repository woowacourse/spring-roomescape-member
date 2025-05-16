package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.controller.annotation.Admin;
import roomescape.reservation.controller.dto.ReservationTimeRequest;
import roomescape.reservation.controller.dto.ReservationTimeResponse;
import roomescape.reservation.service.ReservationTimeService;

@RequestMapping("/times")
@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public List<ReservationTimeResponse> getTimes() {
        return reservationTimeService.getTimes();
    }

    @Admin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReservationTimeResponse addTime(@Valid @RequestBody ReservationTimeRequest request) {
        return reservationTimeService.add(request);
    }

    @Admin
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{timeId}")
    public void deleteTime(@PathVariable("timeId") Long timeId) {
        reservationTimeService.remove(timeId);
    }

}
