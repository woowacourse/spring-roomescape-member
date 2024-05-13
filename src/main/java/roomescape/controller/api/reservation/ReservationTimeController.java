package roomescape.controller.api.reservation;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.ReservationTimeAddRequest;
import roomescape.dto.response.AvailableReservationTimeResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> addTime(
            @RequestBody ReservationTimeAddRequest reservationTimeAddRequest) {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.addTime(reservationTimeAddRequest);
        return ResponseEntity.created(URI.create("/times/" + reservationTimeResponse.id()))
                .body(reservationTimeResponse);
    }

    @GetMapping
    public List<AvailableReservationTimeResponse> findTimes(@RequestParam(required = false) String date, @RequestParam(required = false) Long themeId) {
        return reservationTimeService.findAvailableTimes(date, themeId);
    }

    @GetMapping("/{id}")
    public ReservationTimeResponse getTime(@PathVariable Long id) {
        return reservationTimeService.getTime(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        reservationTimeService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}
