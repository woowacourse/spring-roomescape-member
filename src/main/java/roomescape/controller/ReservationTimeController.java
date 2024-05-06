package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.request.ReservationTimeRequest;
import roomescape.controller.response.ReservationTimeResponse;
import roomescape.exception.BadRequestException;
import roomescape.model.ReservationTime;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.ReservationTimeDto;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> showReservationTimes() {
        List<ReservationTime> times = reservationTimeService.findAllReservationTimes();
        List<ReservationTimeResponse> response = times.stream()
                .map(ReservationTimeResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> addReservationTime(@RequestBody ReservationTimeRequest request) {
        ReservationTimeDto timeDto = ReservationTimeDto.from(request);
        ReservationTime time = reservationTimeService.saveReservationTime(timeDto);
        ReservationTimeResponse response = ReservationTimeResponse.from(time);
        return ResponseEntity
                .created(URI.create("/times/" + response.getId()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("id") Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
