package roomescape.reservation.presentation;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.presentation.dto.ReservationTimeRequest;
import roomescape.reservation.presentation.dto.ReservationTimeResponse;
import roomescape.reservation.application.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createTime(
            final @RequestBody ReservationTimeRequest reservationTimeRequest
    ) {
        return ResponseEntity.ok().body(
                reservationTimeService.createReservationTime(reservationTimeRequest)
        );
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes(
    ) {
        return ResponseEntity.ok().body(
                reservationTimeService.getReservationTimes()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(
            final @PathVariable Long id
    ) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
