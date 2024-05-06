package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.service.dto.ReservationTimeBookedRequest;
import roomescape.service.dto.ReservationTimeBookedResponse;
import roomescape.service.dto.ReservationTimeResponse;
import roomescape.service.dto.ReservationTimeSaveRequest;
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

    @GetMapping(params = {"date", "themeId"})
    public ResponseEntity<List<ReservationTimeBookedResponse>> getTimesWithBooked(@ModelAttribute @Valid final ReservationTimeBookedRequest reservationTimeBookedRequest) {
        final List<ReservationTimeBookedResponse> reservationTimeBookedResponses = reservationTimeService.getTimesWithBooked(reservationTimeBookedRequest);
        return ResponseEntity.ok(reservationTimeBookedResponses);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getTimes() {
        final List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.getTimes();
        return ResponseEntity.ok(reservationTimeResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> saveTime(@RequestBody @Valid final ReservationTimeSaveRequest reservationTimeSaveRequest) {
        final ReservationTimeResponse reservationTimeResponse = reservationTimeService.saveTime(reservationTimeSaveRequest);
        return ResponseEntity.created(URI.create("/times/" + reservationTimeResponse.id()))
                .body(reservationTimeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable("id") final Long id) {
        reservationTimeService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}
