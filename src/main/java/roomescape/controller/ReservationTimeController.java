package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeCreationRequest;
import roomescape.dto.ReservationTimeWithBookState;
import roomescape.service.ReservationTimeService;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public List<ReservationTime> getReservationTimes() {
        return reservationTimeService.getAllReservationTime();
    }

    @GetMapping("/{date}/{themeId}/times")
    public List<ReservationTimeWithBookState> getReservationTimesInThemeAndDate(
            @PathVariable("date")LocalDate date, @PathVariable("themeId") Long themId) {
        return reservationTimeService.getAllReservationTimeWithBookState(date,themId);
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTime> createReservationTime(
            @RequestBody ReservationTimeCreationRequest request
    ) {
        long savedId = reservationTimeService.saveReservationTime(request);
        ReservationTime savedTime = reservationTimeService.getReservationTimeById(savedId);
        return ResponseEntity.created(URI.create("times/" + savedId)).body(savedTime);
    }

    @DeleteMapping("/times/{reservationTimeId}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("reservationTimeId") Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
