package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RestController
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> read() {
        List<ReservationTimeResponse> responses = reservationTimeService.findAll();
        return ResponseEntity.ok(responses);
    }
}
