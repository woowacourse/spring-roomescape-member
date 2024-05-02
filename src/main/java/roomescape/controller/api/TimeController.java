package roomescape.controller.api;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RequestMapping("/times")
@RestController
public class TimeController {

    ReservationTimeService reservationTimeService;

    public TimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getTimes() {
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.getAllReservationTimes();

        return ResponseEntity.ok()
                .body(reservationTimes);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> addTime(@RequestBody ReservationTimeRequest reservationTimeRequest) {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.addReservationTime(
                reservationTimeRequest);

        return ResponseEntity.created(URI.create("/times/" + reservationTimeResponse.id()))
                .body(reservationTimeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable("id") Long id) {
        reservationTimeService.deleteReservationTimeById(id);

        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableReservationTimeResponse>> getATimes(@RequestParam LocalDate date,
                                                                            @RequestParam Long themeId) {
        List<AvailableReservationTimeResponse> availableReservationTimes = reservationTimeService.getAvailableReservationTime(
                date, themeId);

        return ResponseEntity.ok()
                .body(availableReservationTimes);
    }
}
