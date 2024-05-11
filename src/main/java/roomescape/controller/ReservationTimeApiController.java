package roomescape.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.AvailableTimeRequest;
import roomescape.service.dto.AvailableTimeResponses;
import roomescape.service.dto.ReservationTimeRequest;
import roomescape.service.dto.ReservationTimeResponse;

@RestController
public class ReservationTimeApiController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeApiController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public List<ReservationTimeResponse> findReservationTimes() {
        return reservationTimeService.findAllReservationTimes();
    }

    @GetMapping("/times/available")
    public AvailableTimeResponses findAvailableReservationTimes(@Valid AvailableTimeRequest requestDto) {
        return reservationTimeService.findAvailableReservationTimes(requestDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/times")
    public ReservationTimeResponse createReservationTime(@Valid @RequestBody ReservationTimeRequest requestDto) {
        return reservationTimeService.createReservationTime(requestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/times/{id}")
    public void deleteReservationTime(@PathVariable long id) {
        reservationTimeService.deleteReservationTime(id);
    }
}
