package roomescape.domain.reservationtime.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservationtime.request.ReservationTimeCreateRequest;
import roomescape.domain.reservationtime.request.ReservationTimeUpdateRequest;
import roomescape.domain.reservationtime.response.ReservationTimeResponse;
import roomescape.domain.reservationtime.service.ReservationTimeService;

@RestController
@RequestMapping("/admin/times")
public class AdminReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public AdminReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> save(@RequestBody @Valid ReservationTimeCreateRequest request) {
        ReservationTimeResponse response = reservationTimeService.saveReservationTime(request);
        return ResponseEntity.created(URI.create("/times/" + response.id()))
                .body(response);
    }

    @PatchMapping("/{reservationTimeId}")
    public ResponseEntity<ReservationTimeResponse> update(
            @PathVariable Long reservationTimeId,
            @RequestBody @Valid ReservationTimeUpdateRequest request
    ) {
        ReservationTimeResponse response = reservationTimeService.updateReservationTime(reservationTimeId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reservationTimeId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long reservationTimeId) {
        reservationTimeService.deleteReservationTimeBy(reservationTimeId);
        return ResponseEntity.noContent().build();
    }
}
