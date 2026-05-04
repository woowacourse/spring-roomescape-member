package roomescape.time.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.dto.CreateReservationTimeRequest;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> read() {
        return ResponseEntity.ok(reservationTimeService.findAll());
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(
            @RequestBody CreateReservationTimeRequest createReservationTimeRequest) {
        return ResponseEntity.ok(reservationTimeService.create(createReservationTimeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationTimeResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(reservationTimeService.delete(id));
    }
}
