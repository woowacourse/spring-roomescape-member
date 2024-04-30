package roomescape.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

import java.util.List;

@RequestMapping("/times")
@RestController
public class TimeController {

    ReservationTimeService reservationTimeService;

    public TimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    ResponseEntity<List<ReservationTimeResponse>> getTimes() {
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.getAllReservationTimes();

        return ResponseEntity.ok()
                .body(reservationTimes);
    }

    @PostMapping
    ResponseEntity<ReservationTimeResponse> addTime(@RequestBody ReservationTimeRequest reservationTimeRequest) {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.addReservationTime(reservationTimeRequest);

        return ResponseEntity.ok()
                .body(reservationTimeResponse);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTime(@PathVariable("id") Long id) {
        reservationTimeService.deleteReservationTimeById(id);

        return ResponseEntity.ok()
                .build();
    }
}
