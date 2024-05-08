package roomescape.reservationtime.controller;

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
import roomescape.reservationtime.dto.request.CreateReservationTimeRequest;
import roomescape.reservationtime.dto.response.CreateReservationTimeResponse;
import roomescape.reservationtime.dto.response.FindReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping("/times")
    public ResponseEntity<CreateReservationTimeResponse> createReservationTime(@RequestBody final CreateReservationTimeRequest createReservationTimeRequest) {
        CreateReservationTimeResponse reservationTime = reservationTimeService.createReservationTime(createReservationTimeRequest);
        return ResponseEntity.created(URI.create("/times/" + reservationTime.id())).body(reservationTime);
    }

    @GetMapping("/times")
    public ResponseEntity<List<FindReservationTimeResponse>> getReservationTimes() {
        List<FindReservationTimeResponse> reservationTimeResponses = reservationTimeService.getReservationTimes();
        return ResponseEntity.ok(reservationTimeResponses);
    }

    @GetMapping("/times/{id}")
    public ResponseEntity<FindReservationTimeResponse> getReservationTime(@PathVariable final Long id) {
        return ResponseEntity.ok(reservationTimeService.getReservationTime(id));
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable final Long id) {
        reservationTimeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
