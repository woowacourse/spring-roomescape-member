package roomescape.reservationtime.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservationtime.service.AdminReservationTimeService;
import roomescape.reservationtime.domain.ReservationTime;

@RestController
@RequestMapping("/admin/times")
public class AdminReservationTimeController {
    private final AdminReservationTimeService adminReservationTimeService;

    public AdminReservationTimeController(AdminReservationTimeService adminReservationTimeService) {
        this.adminReservationTimeService = adminReservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(
            @Valid @RequestBody ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = adminReservationTimeService.createReservationTime(
                reservationTimeRequest.startAt());
        ReservationTimeResponse response = ReservationTimeResponse.from(reservationTime);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable long id) {
        adminReservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
