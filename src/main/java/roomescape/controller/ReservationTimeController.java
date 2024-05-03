package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.request.ReservationTimeRequest;
import roomescape.model.ReservationTime;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTime>> getReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.findAllReservationTimes();
        return ResponseEntity.ok(reservationTimes);
    }

    @PostMapping
    public ResponseEntity<ReservationTime> createReservationTime(@RequestBody ReservationTimeRequest request) {
        ReservationTime reservationTime = reservationTimeService.addReservationTime(request);
        return ResponseEntity.created(URI.create("/times/" + reservationTime.getId())).body(reservationTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("id") long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.ok().build();
    }
}
