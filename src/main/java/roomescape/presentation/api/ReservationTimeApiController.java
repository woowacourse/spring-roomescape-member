package roomescape.presentation.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.presentation.dto.request.ReservationTimeRequest;
import roomescape.presentation.dto.response.ReservationTimeResponse;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.service.ReservationTimeService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
public class ReservationTimeApiController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeApiController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> createReservationTime(@RequestBody @Valid ReservationTimeRequest request) {
        ReservationTime reservationTime = reservationTimeService.createReservationTime(request.startAtToLocalTime());
        ReservationTimeResponse response = ReservationTimeResponse.from(reservationTime);
        return ResponseEntity.created(URI.create("/times")).body(response);
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> getAllReservationTime() {
        List<ReservationTime> reservationTimes = reservationTimeService.getAllReservationTime();
        List<ReservationTimeResponse> responses = ReservationTimeResponse.from(reservationTimes);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/times/possible")
    public ResponseEntity<List<ReservationTimeResponse>> getAvailableReservationTimes(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") long themeId
    ) {
        List<ReservationTime> reservationTimes = reservationTimeService.getAvailableReservationTimesOf(date, themeId);
        List<ReservationTimeResponse> responses = ReservationTimeResponse.from(reservationTimes);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
