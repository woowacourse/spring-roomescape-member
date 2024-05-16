package roomescape.controller.api;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.api.dto.request.ReservationTimeCreateRequest;
import roomescape.controller.api.dto.response.ReservationTimeResponse;
import roomescape.controller.api.dto.response.ReservationTimesResponse;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("admin/times")
public class AdminReservationTimeApiController {

    private final ReservationTimeService reservationTimeService;

    public AdminReservationTimeApiController(final ReservationTimeService reservationTimeService) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable final long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
