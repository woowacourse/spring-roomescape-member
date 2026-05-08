package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ReservationTimeRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RequestMapping("/times")
@RestController
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getTimes() {
        final List<ReservationTimeResponse> responses = reservationTimeService.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createTime(@RequestBody ReservationTimeRequest request) {
        final ReservationTimeResponse response = ReservationTimeResponse.from(
                reservationTimeService.save(request.startAt()));
        final URI location = URI.create("/times/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}