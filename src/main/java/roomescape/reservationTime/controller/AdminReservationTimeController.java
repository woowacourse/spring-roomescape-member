package roomescape.reservationTime.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.AdminReservationTimeRequest;
import roomescape.reservationTime.dto.AdminReservationTimeResponse;
import roomescape.reservationTime.service.ReservationTimeService;

@RestController
@RequestMapping("/admin")
public class AdminReservationTimeController {

    private final ReservationTimeService timeService;

    public AdminReservationTimeController(ReservationTimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping("/times")
    public ResponseEntity<AdminReservationTimeResponse> createTime(@Valid @RequestBody AdminReservationTimeRequest request) {
        ReservationTime time = timeService.addReservationTime(request.startAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(AdminReservationTimeResponse.from(time));
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        timeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
