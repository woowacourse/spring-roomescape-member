package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.ReservationTimeRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.domain.ReservationTime;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/times")
public class AdminReservationTimeController {

    private final ReservationTimeService service;

    public AdminReservationTimeController(ReservationTimeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        List<ReservationTimeResponse> times = service.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
        return ResponseEntity.ok(times);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(@RequestBody ReservationTimeRequest request) {
        ReservationTime reservationTime = service.create(request.startAt());
        return ResponseEntity.created(URI.create("/admin/times/" + reservationTime.getId()))
                .body(ReservationTimeResponse.from(reservationTime));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
