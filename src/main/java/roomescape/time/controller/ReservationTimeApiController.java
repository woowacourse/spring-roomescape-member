package roomescape.time.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.dto.RequestTime;
import roomescape.time.dto.ResponseTime;
import roomescape.time.service.ReservationTimeService;

@RestController
public class ReservationTimeApiController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeApiController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ResponseTime>> findAll() {
        List<ResponseTime> times = reservationTimeService.findAll();

        return ResponseEntity.ok(times);
    }

    @PostMapping("/times")
    public ResponseEntity<ResponseTime> save(@RequestBody RequestTime requestTime) {
        Long saveId = reservationTimeService.save(requestTime);
        ResponseTime responseTime = reservationTimeService.findById(saveId);

        return ResponseEntity.ok(responseTime);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.delete(id);

        return ResponseEntity.ok().build();
    }
}
