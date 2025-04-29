package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.request.CreateReservationTimeRequest;
import roomescape.controller.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;
import roomescape.service.result.ReservationTimeResult;

import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationService;

    public ReservationTimeController(ReservationTimeService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(
            @RequestBody CreateReservationTimeRequest createReservationTImeRequest) {
        Long id = reservationService.create(createReservationTImeRequest.toServiceParam());
        ReservationTimeResult reservationTimeResult = reservationService.findById(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservationTimeResponse.from(reservationTimeResult));
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findAll() {
        List<ReservationTimeResult> reservationTimeResults = reservationService.findAll();
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeResults.stream()
                .map(ReservationTimeResponse::from)
                .toList();
        return ResponseEntity.ok(reservationTimeResponses);
    }

    @DeleteMapping("/{reservationTimeId}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("reservationTimeId") Long reservationTimeId) {
        reservationService.deleteById(reservationTimeId);
        return ResponseEntity.noContent().build();
    }
}
