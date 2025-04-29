package roomescape.reservationtime.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.dto.request.ReservationTimeRequest;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;

@RestController
@RequestMapping(value = "/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping()
    public List<ReservationTimeResponse> findAll() {
        return reservationTimeService.findAll();
    }

    @PostMapping()
    public Long create(@Valid @RequestBody ReservationTimeRequest reservationTimeRequest) {
        return reservationTimeService.create(reservationTimeRequest);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return reservationTimeService.delete(id);
    }
}
