package roomescape.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.domain.ReservationTime;
import roomescape.service.ReservationTimeService;

public class AdminReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public AdminReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTime> create(@RequestBody ReservationTime reservationTime) {
        return ResponseEntity.ok(reservationTimeService.save(reservationTime));
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
