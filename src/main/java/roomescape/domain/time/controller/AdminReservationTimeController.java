package roomescape.domain.time.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.time.request.ReservationTimeCreateRequest;
import roomescape.domain.time.response.ReservationTimeResponse;
import roomescape.domain.time.service.ReservationTimeService;

@RestController
@RequestMapping("/admin/times")
public class AdminReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public AdminReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> save(@RequestBody ReservationTimeCreateRequest request) {
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
