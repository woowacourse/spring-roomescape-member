package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.CreateResrvationTimeRequest;
import roomescape.controller.dto.GetAvailableTimesRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.controller.dto.ThemeReservationTimesResponse;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
@RequiredArgsConstructor
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        return ResponseEntity.ok(reservationTimeService.findAllReservationTimes());
    }

    @GetMapping(params = {"themeId", "date"})
    public ResponseEntity<ThemeReservationTimesResponse> getAvailableTimes(
            @RequestParam Long themeId,
            @RequestParam LocalDate date,
            @RequestParam(required = false) Boolean available
    ) {
        return ResponseEntity.ok(reservationTimeService.findAllAvailableTimes(GetAvailableTimesRequest.of(themeId, date, available)));
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(
            @RequestBody CreateResrvationTimeRequest createResrvationTimeRequest) {
        ReservationTimeResponse createdReservationTime = reservationTimeService.addReservationTime(
                createResrvationTimeRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdReservationTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.removeRegisteredReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
