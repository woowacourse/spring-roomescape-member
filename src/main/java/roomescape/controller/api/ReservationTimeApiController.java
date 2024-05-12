package roomescape.controller.api;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.api.dto.request.ReservationTimeCreateRequest;
import roomescape.controller.api.dto.response.AvailableReservationTimesResponse;
import roomescape.controller.api.dto.response.ReservationTimeResponse;
import roomescape.controller.api.dto.response.ReservationTimesResponse;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeApiController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeApiController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(
            @RequestBody final ReservationTimeCreateRequest request) {
        final var output = reservationTimeService.createReservationTime(request.toInput());
        return ResponseEntity.created(URI.create("/times/" + output.id()))
                .body(ReservationTimeResponse.from(output));
    }

    @GetMapping
    public ResponseEntity<ReservationTimesResponse> getAllReservationTimes() {
        final var outputs = reservationTimeService.getAllReservationTimes();
        return ResponseEntity.ok(ReservationTimesResponse.from(outputs));
    }

    @GetMapping("/available")
    public ResponseEntity<AvailableReservationTimesResponse> findAvailableReservationTimes(
            @RequestParam final String date,
            @RequestParam final long themeId) {
        final var outputs = reservationTimeService.findAvailableReservationTimes(date, themeId);
        return ResponseEntity.ok(AvailableReservationTimesResponse.from(outputs));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable final long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
