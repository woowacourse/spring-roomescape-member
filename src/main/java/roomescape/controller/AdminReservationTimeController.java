package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.ReservationTime;
import roomescape.controller.dto.ReservationTimeCreateRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/admin/times")
public class AdminReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public AdminReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(@RequestBody @Valid ReservationTimeCreateRequest request) {
        ReservationTime reservationTime = reservationTimeService.create(request.startAt());

        return ResponseEntity.status(CREATED)
                .body(ReservationTimeResponse.from(reservationTime));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
