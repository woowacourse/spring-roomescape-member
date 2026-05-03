package roomescape.reservationtime;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.dto.ReservationTimeRequest;
import roomescape.reservationtime.dto.ReservationTimeResponse;

@RestController
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public List<ReservationTimeResponse> getTimes() {
        return reservationTimeService.findAll();
    }

    @PostMapping("/times")
    public ReservationTimeResponse createTime(@RequestBody ReservationTimeRequest request) {
        return reservationTimeService.save(request);
    }

    @DeleteMapping("/times/{id}")
    public void deleteTime(@PathVariable Long id) {
        reservationTimeService.delete(id);
    }
}
