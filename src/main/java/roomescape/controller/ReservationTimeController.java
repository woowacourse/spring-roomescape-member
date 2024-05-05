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
import roomescape.service.dto.response.ReservationTimeIsBookedResponse;
import roomescape.service.dto.response.ReservationTimeResponse;
import roomescape.service.dto.request.SaveReservationTimeRequest;
import roomescape.service.reservationtime.ReservationTimeCreateService;
import roomescape.service.reservationtime.ReservationTimeDeleteService;
import roomescape.service.reservationtime.ReservationTimeFindService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class ReservationTimeController {

    private final ReservationTimeCreateService reservationTimeCreateService;
    private final ReservationTimeFindService reservationTimeFindService;
    private final ReservationTimeDeleteService reservationTimeDeleteService;

    public ReservationTimeController(ReservationTimeCreateService reservationTimeCreateService,
                                     ReservationTimeFindService reservationTimeFindService,
                                     ReservationTimeDeleteService reservationTimeDeleteService) {
        this.reservationTimeCreateService = reservationTimeCreateService;
        this.reservationTimeFindService = reservationTimeFindService;
        this.reservationTimeDeleteService = reservationTimeDeleteService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeFindService.findReservationTimes();
        return ResponseEntity.ok(ReservationTimeResponse.listOf(reservationTimes));
    }

    @GetMapping("/times/available")
    public ResponseEntity<List<ReservationTimeIsBookedResponse>> getReservationTimesIsBooked(@RequestParam LocalDate date,
                                                                                             @RequestParam Long themeId) {
        Map<ReservationTime, Boolean> isBooked = reservationTimeFindService.findIsBooked(date, themeId);
        return ResponseEntity.ok(ReservationTimeIsBookedResponse.listOf(isBooked));
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> addReservationTime(@RequestBody SaveReservationTimeRequest request) {
        ReservationTime reservationTime = reservationTimeCreateService.createReservationTime(request);
        return ResponseEntity.created(URI.create("times/" + reservationTime.getId()))
                .body(new ReservationTimeResponse(reservationTime));
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeDeleteService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
