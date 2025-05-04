package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping(value = "/times", params = {"date", "themeId"})
    public List<TimeWithBookStateResponse> getReservationTimesInThemeAndDate(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") Long themeId
    ) {
        List<TimeWithBookState> times = reservationTimeService
                .getAllReservationTimeWithBookState(date, themeId);
        return times.stream()
                .map(TimeWithBookStateResponse::new)
                .toList();
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> createReservationTime(
            @Valid @RequestBody ReservationTimeCreationRequest request
    ) {
        long savedId = reservationTimeService.saveReservationTime(request);
        ReservationTime savedTime = reservationTimeService.getReservationTimeById(savedId);
        return ResponseEntity
                .created(URI.create("/times/" + savedId))
                .body(new ReservationTimeResponse(savedTime));
    }

    @DeleteMapping("/times/{reservationTimeId}")
    public ResponseEntity<Void> deleteReservationTime(
            @PathVariable("reservationTimeId") Long id
    ) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
