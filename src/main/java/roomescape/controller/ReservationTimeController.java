package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.request.ReservationTimeCreateRequest;
import roomescape.service.dto.response.ReservationTimeResponse;
import roomescape.service.dto.response.ReservationTimeStatusResponse;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/times")
@RequiredArgsConstructor
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getTimes() {
        final List<ReservationTimeResponse> results = reservationTimeService.getTimes();
        return ResponseEntity.ok(results);
    }

    @GetMapping(params = {"date", "themeId"})
    public ResponseEntity<List<ReservationTimeStatusResponse>> getTimesWithStatus(
            @RequestParam(value = "date", required = false) LocalDate date,
            @RequestParam(value = "themeId", required = false) Long themeId
    ) {
        final List<ReservationTimeStatusResponse> results = reservationTimeService.getTimesWithStatus(date, themeId);
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(
            @RequestBody ReservationTimeCreateRequest request
    ) {
        final ReservationTimeResponse result = reservationTimeService.create(request);
        return ResponseEntity.created(URI.create("/times"))
                .body(result);
    }

    @DeleteMapping("/{time-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("time-id") Long timeId
    ) {
        reservationTimeService.delete(timeId);
        return ResponseEntity.noContent().build();
    }
}
