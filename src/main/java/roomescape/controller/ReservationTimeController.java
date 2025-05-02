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
import roomescape.dto.other.TimeWithBookState;
import roomescape.dto.request.ReservationTimeCreationRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.TimeWithBookStateResponse;
import roomescape.service.ReservationTimeService;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public List<ReservationTimeResponse> getReservationTimes() {
        List<ReservationTime> times = reservationTimeService.getAllReservationTime();
        return times.stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    @GetMapping("/{date}/{themeId}/times")
    public List<TimeWithBookStateResponse> getReservationTimesInThemeAndDate(
            @PathVariable("date") LocalDate date,
            @PathVariable("themeId") Long themId
    ) {
        List<TimeWithBookState> times = reservationTimeService
                .getAllReservationTimeWithBookState(date, themId);
        return times.stream()
                .map(TimeWithBookStateResponse::new)
                .toList();
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> createReservationTime(
            @RequestBody ReservationTimeCreationRequest request
    ) {
        long savedId = reservationTimeService.saveReservationTime(request);
        ReservationTime savedTime = reservationTimeService.getReservationTimeById(savedId);
        return ResponseEntity
                .created(URI.create("times/" + savedId))
                .body(new ReservationTimeResponse(savedTime));
    }

    @DeleteMapping("/times/{reservationTimeId}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("reservationTimeId") Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
