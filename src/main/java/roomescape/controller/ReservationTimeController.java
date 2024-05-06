package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.service.dto.ReservationTimeIsBookedResponse;
import roomescape.service.dto.ReservationTimeResponse;
import roomescape.service.dto.SaveReservationTimeRequest;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.findReservationTimes();
        return ResponseEntity.ok(ReservationTimeResponse.listOf(reservationTimes));
    }

    @GetMapping("/times/available")
    public ResponseEntity<List<ReservationTimeIsBookedResponse>> getReservationTimesIsBooked(@RequestParam LocalDate date,
                                                                                             @RequestParam Long themeId) {
        Map<ReservationTime, Boolean> isBooked = reservationTimeService.findIsBooked(date, themeId);
        return ResponseEntity.ok(ReservationTimeIsBookedResponse.listOf(isBooked));
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> addReservationTime(@RequestBody SaveReservationTimeRequest request) {
        ReservationTime reservationTime = reservationTimeService.createReservationTime(request);
        return ResponseEntity.created(URI.create("times/" + reservationTime.getId()))
                .body(ReservationTimeResponse.of(reservationTime));
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
