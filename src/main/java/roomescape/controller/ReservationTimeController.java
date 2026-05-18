package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.AvailableTimeFindRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.domain.ReservationTime;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> reservationTimes = reservationTimeService.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponse::toDto)
                .toList();
    }

    @GetMapping("/available")
    public List<ReservationTimeResponse> findAvailable(@ModelAttribute AvailableTimeFindRequest request) {
        return reservationTimeService.findAvailable(request).stream()
                .map(ReservationTimeResponse::toDto)
                .toList();
    }
}
