package roomescape.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ReservationTimeRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.domain.ReservationTime;
import roomescape.service.ReservationTimeService;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public List<ReservationTimeResponse> list() {
        return reservationTimeService.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @PostMapping("/times")
    public ReservationTimeResponse create(@RequestBody ReservationTimeRequest request) {
        ReservationTime saved = reservationTimeService.create(request.toCommand());
        return ReservationTimeResponse.from(saved);
    }

    @DeleteMapping("/times/{id}")
    public void delete(@PathVariable Long id) {
        reservationTimeService.delete(id);
    }
}
