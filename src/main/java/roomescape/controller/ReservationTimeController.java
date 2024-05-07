package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.ReservationTimeIsBookedResponse;
import roomescape.service.dto.ReservationTimeResponse;
import roomescape.service.dto.ReservationTimeSaveRequest;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.findReservationTimes();
        return ResponseEntity.ok(ReservationTimeResponse.listOf(reservationTimes));
    }

    @GetMapping("/available")
    public ResponseEntity<List<ReservationTimeIsBookedResponse>> getReservationTimesIsBooked(@RequestParam LocalDate date,
                                                                                             @RequestParam Long themeId) {
        Map<ReservationTime, Boolean> isBooked = reservationTimeService.findIsBooked(date, themeId);
        return ResponseEntity.ok(ReservationTimeIsBookedResponse.listOf(isBooked));
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> addReservationTime(@RequestBody @Valid ReservationTimeSaveRequest request) {
        ReservationTime reservationTime = reservationTimeService.createReservationTime(request);
        return ResponseEntity.created(URI.create("/times/" + reservationTime.getId()))
                .body(ReservationTimeResponse.of(reservationTime));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
