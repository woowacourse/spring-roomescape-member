package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.request.ReservationTimeCreateRequest;
import roomescape.controller.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.response.ReservationTimeResult;

import java.util.List;

@RestController
@RequestMapping("/times")
@RequiredArgsConstructor
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getTimes() {
        final List<ReservationTimeResult> results = reservationTimeService.getTimes();
        return ResponseEntity.ok(ReservationTimeResponse.from(results));
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(
            @RequestBody ReservationTimeCreateRequest request
    ) {
        final ReservationTimeResult result = reservationTimeService.create(request.toData());
        return ResponseEntity.ok(ReservationTimeResponse.from(result));
    }

    @DeleteMapping("/{time-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("time-id") Long timeId
    ) {
        reservationTimeService.delete(timeId);
        return ResponseEntity.noContent().build();
    }
}
