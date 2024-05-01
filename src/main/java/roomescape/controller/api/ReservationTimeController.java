package roomescape.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ReservationTimeSaveRequest;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getTimes() {
        final List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.getTimes();
        return ResponseEntity.ok(reservationTimeResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> saveTime(@RequestBody final ReservationTimeSaveRequest reservationTimeSaveRequest) {
        final ReservationTimeResponse reservationTimeResponse = reservationTimeService.saveTime(reservationTimeSaveRequest);
        return ResponseEntity.created(URI.create("/times/" + reservationTimeResponse.id()))
                .body(reservationTimeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(final @PathVariable("id") Long id) {
        reservationTimeService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}
