package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationTimeService;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@RestController
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> getTimes() {
        return ResponseEntity.ok(reservationTimeService.findAll());
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> createTime(@RequestBody ReservationTimeRequest request) {
        final ReservationTimeResponse response = reservationTimeService.save(request);
        final URI location = URI.create("/times/" + response.id());

        return ResponseEntity.created(location)
                .body(response);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
