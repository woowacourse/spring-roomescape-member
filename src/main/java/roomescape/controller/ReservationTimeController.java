package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationTimeRequest;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<Object> reservationTimeList() {
        return ResponseEntity.ok(reservationTimeService.findReservationTimes());
    }

    @PostMapping
    public ResponseEntity<Object> reservationTimeAdd(
            @Valid @RequestBody ReservationTimeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationTimeService.addReservationTime(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> reservationTimeRemove(@PathVariable(name = "id") long id) {
        reservationTimeService.removeReservationTime(id);
        return ResponseEntity.noContent().build();
    }

}
