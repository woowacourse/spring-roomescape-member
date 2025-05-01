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
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService service;

    public ReservationTimeController(ReservationTimeService service) {
        this.service = service;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> readAllReservationTime() {
        return ResponseEntity.ok(service.readAllReservationTime());
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> postReservationTime(@RequestBody ReservationTimeRequest request) {
        ReservationTimeResponse reservationTimeResponse = service.postReservationTime(request);
        URI location = URI.create("/times/" + reservationTimeResponse.id());
        return ResponseEntity.created(location).body(reservationTimeResponse);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable long id) {
        service.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
