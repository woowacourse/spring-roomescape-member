package roomescape.domain.time.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.time.request.ReservationTimeCreateRequest;
import roomescape.domain.time.response.ReservationTimeResponse;
import roomescape.domain.time.service.ReservationTimeService;

import java.net.URI;

@RestController
@RequestMapping("/admin/times")
public class AdminReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public AdminReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> save(@Valid @RequestBody ReservationTimeCreateRequest request) {
        ReservationTimeResponse response = reservationTimeService.saveReservationTime(request);
        return ResponseEntity.created(URI.create("/times/" + response.id()))
                .body(response);
    }

    @DeleteMapping("/{reservationTimeId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long reservationTimeId) {
        reservationTimeService.deleteReservationTimeBy(reservationTimeId);
        return ResponseEntity.noContent().build();
    }
}
