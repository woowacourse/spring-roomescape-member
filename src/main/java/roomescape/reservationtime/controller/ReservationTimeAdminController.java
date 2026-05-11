package roomescape.reservationtime.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.ReservationTimeCreateRequest;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/admin/times")
public class ReservationTimeAdminController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeAdminController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(
            @Valid @RequestBody ReservationTimeCreateRequest request) {
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
