package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeAddRequest;
import roomescape.service.ReservationTimeService;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTime>> getReservationTimeList() {
        return ResponseEntity.ok(reservationTimeService.findAllReservationTime());
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTime> addReservationTime(@RequestBody ReservationTimeAddRequest reservationTimeAddRequest) {
        return ResponseEntity.ok(reservationTimeService.addReservationTime(reservationTimeAddRequest));
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("id") Long id) {
        reservationTimeService.removeReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
