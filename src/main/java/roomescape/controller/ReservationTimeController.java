package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.SaveReservationTimeRequest;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationTimeService.getReservationTimes()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> saveReservationTime(@RequestBody final SaveReservationTimeRequest request) {
        final ReservationTime savedReservationTime = reservationTimeService.saveReservationTime(request);

        return ResponseEntity.created(URI.create("/times/" + savedReservationTime.getId()))
                .body(ReservationTimeResponse.from(savedReservationTime));
    }

    @DeleteMapping("/times/{reservation-time-id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("reservation-time-id") final Long reservationTimeId) {
        reservationTimeService.deleteReservationTime(reservationTimeId);
        return ResponseEntity.noContent().build();
    }
}
